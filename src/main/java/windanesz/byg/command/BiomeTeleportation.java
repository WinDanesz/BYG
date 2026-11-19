package windanesz.byg.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import windanesz.byg.BiomesYouGo;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.function.BooleanSupplier;

/**
 * Finds a biome and teleports a player to it. A search does a little work each tick instead of all at once, so the
 * world keeps running while it lasts. Each world runs one search at a time.
 */
@Mod.EventBusSubscriber(modid = BiomesYouGo.MODID)
public final class BiomeTeleportation {
    /** Cells are sampled at biome-generation resolution (4 blocks); a tile covers 64 x 64 cells. */
    public static final int TILE_CELLS = 64;
    private static final int TILE_BLOCKS = TILE_CELLS * 4;
    private static final int MAX_TILE_RADIUS = 48;
    /** How far a search reaches (~ 25,000 blocks). */
    public static final int MAX_SEARCH_BLOCKS = MAX_TILE_RADIUS * TILE_BLOCKS;
    /** A fifth of a tick. */
    private static final long TICK_BUDGET_NANOS = 10000000L;
    private static final int LANDING_CHUNKS = 9;
    private static final Map<World, Search> SEARCHES = new HashMap<>();

    /** Narrows a search down. Every method is called on the server thread. */
    public interface Rules {
        /** Called every tick before the search begins, until it returns true. Has to return once out of time. */
        default boolean prepare(BooleanSupplier outOfTime) {
            return true;
        }

        default boolean accepts(int cellX, int cellZ) {
            return true;
        }

        /** Called once the search is over, with the destination or null if there is none. */
        default void finished(EntityPlayerMP player, @Nullable BlockPos target) {
        }
    }

    private BiomeTeleportation() {
    }

    @Nullable
    public static ResourceLocation resolve(String input) {
        String name = input.toLowerCase(Locale.ROOT);
        String[] candidates = name.indexOf(':') >= 0 ? new String[] {name}
                : new String[] {name, BiomesYouGo.MODID + ":" + name, BiomesYouGo.MODID + ":byg_" + name};
        for (String candidate : candidates) {
            ResourceLocation id = new ResourceLocation(candidate);
            if (Biome.REGISTRY.containsKey(id)) {
                return id;
            }
        }
        return null;
    }

    /** Starts looking for the nearest instance of the biome, which the player is teleported to once it is found. */
    public static boolean teleport(EntityPlayerMP player, Biome biome, ICommandSender feedback) {
        boolean started = start(player, biome, feedback, player.getPosition(), new Rules() {
            @Override
            public void finished(EntityPlayerMP searcher, @Nullable BlockPos target) {
                if (target == null) {
                    feedback.sendMessage(msg(TextFormatting.RED, "message.byg.biome_teleporter.not_found",
                            biome.getBiomeName(), MAX_SEARCH_BLOCKS));
                }
            }
        });
        if (started) {
            feedback.sendMessage(msg(TextFormatting.GRAY, "message.byg.biome_teleporter.searching", biome.getBiomeName()));
        }
        return started;
    }

    /** Starts a search around the origin. Returns false if the player's world is already running one. */
    public static boolean start(EntityPlayerMP player, Biome biome, ICommandSender feedback, BlockPos origin, Rules rules) {
        if (SEARCHES.containsKey(player.world)) {
            feedback.sendMessage(msg(TextFormatting.RED, "message.byg.biome_teleporter.busy"));
            return false;
        }
        SEARCHES.put(player.world, new Search(player.getUniqueID(), biome, feedback, origin, rules));
        return true;
    }

    public static ITextComponent msg(TextFormatting color, String key, Object... args) {
        return new TextComponentTranslation(key, args).setStyle(new Style().setColor(color));
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.world.isRemote) {
            return;
        }
        Search search = SEARCHES.get(event.world);
        if (search == null) {
            return;
        }
        EntityPlayer player = event.world.getPlayerEntityByUUID(search.player);
        long deadline = System.nanoTime() + TICK_BUDGET_NANOS;
        if (!(player instanceof EntityPlayerMP) || !player.isEntityAlive()
                || search.tick((EntityPlayerMP) player, () -> System.nanoTime() >= deadline)) {
            SEARCHES.remove(event.world);
        }
    }

    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload event) {
        // Client worlds unload on the client thread, and never have a search.
        if (!event.getWorld().isRemote) {
            SEARCHES.remove(event.getWorld());
        }
    }

    /** The tile at this position on the square ring of that radius, going clockwise from its north-west corner. */
    private static int[] ringTile(int radius, int index) {
        if (radius == 0) {
            return new int[] {0, 0};
        }
        int side = radius * 2;
        int step = index % side;
        switch (index / side) {
            case 0: return new int[] {step - radius, -radius};
            case 1: return new int[] {radius, step - radius};
            case 2: return new int[] {radius - step, radius};
            default: return new int[] {-radius, radius - step};
        }
    }

    static final class Search {
        private final UUID player;
        private final Biome biome;
        private final ICommandSender feedback;
        private final BlockPos origin;
        private final Rules rules;
        private Biome[] tile;
        private boolean prepared;
        private int radius;
        private int ringIndex;
        private long bestDistance = Long.MAX_VALUE;
        @Nullable
        BlockPos best;
        @Nullable
        private BlockPos target;
        private int landingChunks;

        Search(UUID player, Biome biome, ICommandSender feedback, BlockPos origin, Rules rules) {
            this.player = player;
            this.biome = biome;
            this.feedback = feedback;
            this.origin = origin;
            this.rules = rules;
        }

        /** Returns true when there is nothing left to do. */
        private boolean tick(EntityPlayerMP searcher, BooleanSupplier outOfTime) {
            World world = searcher.world;
            if (!prepared) {
                if (!rules.prepare(outOfTime)) {
                    return false;
                }
                prepared = true;
                if (outOfTime.getAsBoolean()) {
                    return false;
                }
            }
            if (target == null) {
                if (!search(world.getBiomeProvider(), outOfTime)) {
                    return false;
                }
                rules.finished(searcher, best);
                if (best == null) {
                    return true;
                }
                target = best;
                if (outOfTime.getAsBoolean()) {
                    return false;
                }
            }
            // Load the surrounding chunks so decoration (trees etc.) exists before choosing the landing height.
            while (landingChunks < LANDING_CHUNKS) {
                world.getChunk((target.getX() >> 4) + landingChunks % 3 - 1, (target.getZ() >> 4) + landingChunks / 3 - 1);
                landingChunks++;
                if (outOfTime.getAsBoolean()) {
                    return false;
                }
            }
            land(searcher);
            return true;
        }

        /**
         * Spirals outwards tile by tile, leaving the accepted matching cell closest to the origin in {@link #best}.
         * Returns true once a ring held a match or the whole range was searched, false if it ran out of time.
         */
        boolean search(BiomeProvider provider, BooleanSupplier outOfTime) {
            while (radius <= MAX_TILE_RADIUS) {
                int[] offset = ringTile(radius, ringIndex);
                scan(provider, offset[0], offset[1]);
                if (++ringIndex >= radius * 8) {
                    if (best != null) {
                        return true;
                    }
                    ringIndex = 0;
                    radius++;
                }
                if (outOfTime.getAsBoolean()) {
                    break;
                }
            }
            return radius > MAX_TILE_RADIUS;
        }

        private void scan(BiomeProvider provider, int tileX, int tileZ) {
            int cellX = (origin.getX() >> 2) + tileX * TILE_CELLS - TILE_CELLS / 2;
            int cellZ = (origin.getZ() >> 2) + tileZ * TILE_CELLS - TILE_CELLS / 2;
            tile = provider.getBiomesForGeneration(tile, cellX, cellZ, TILE_CELLS, TILE_CELLS);
            for (int i = 0; i < TILE_CELLS * TILE_CELLS; i++) {
                if (tile[i] != biome || !rules.accepts(cellX + i % TILE_CELLS, cellZ + i / TILE_CELLS)) {
                    continue;
                }
                int x = (cellX + i % TILE_CELLS) * 4 + 2;
                int z = (cellZ + i / TILE_CELLS) * 4 + 2;
                long dx = x - origin.getX();
                long dz = z - origin.getZ();
                long distance = dx * dx + dz * dz;
                if (distance < bestDistance) {
                    bestDistance = distance;
                    best = new BlockPos(x, 0, z);
                }
            }
        }

        private void land(EntityPlayerMP searcher) {
            BlockPos ground = searcher.world.getTopSolidOrLiquidBlock(target);
            BlockPos from = searcher.getPosition();
            searcher.dismountRidingEntity();
            searcher.connection.setPlayerLocation(ground.getX() + 0.5, ground.getY() + 1, ground.getZ() + 0.5,
                    searcher.rotationYaw, searcher.rotationPitch);
            searcher.fallDistance = 0;
            feedback.sendMessage(msg(TextFormatting.GREEN, "message.byg.biome_teleporter.teleported",
                    biome.getBiomeName(), ground.getX(), ground.getZ(),
                    ground.getX() - from.getX(), ground.getY() + 1 - from.getY(), ground.getZ() - from.getZ()));
        }
    }
}
