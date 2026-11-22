package windanesz.byg.worldgen;

import net.minecraft.block.BlockFalling;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import windanesz.byg.BiomesYouGo;
import windanesz.byg.biome.BiomeDeadSea;
import windanesz.byg.biome.BiomeStonePillarSavanna;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Structures wider than the area a chunk may touch while it populates. Population only queues them, and each one
 * builds once every chunk within its reach is loaded and populated, so it never generates chunks (cascading worldgen)
 * and never depends on which neighbours happened to be loaded.
 */
@Mod.EventBusSubscriber(modid = BiomesYouGo.MODID)
public class DeferredStructureManager extends WorldSavedData {
    private static final String NAME = BiomesYouGo.MODID + "_deferred_structures";
    private static final Logger LOGGER = LogManager.getLogger(BiomesYouGo.MODID);
    // Entries looked at per tick, so a long queue of structures waiting at the edge of the explored area stays cheap
    private static final int CHECKS_PER_TICK = 32;

    public enum Kind {
        STONE_PILLAR_MESA(48, BiomeStonePillarSavanna::buildPillar),
        DEAD_SEA_SHIPWRECK(26, BiomeDeadSea::buildShipwreck);

        /** Furthest horizontal distance from the origin that the builder reads or writes. */
        private final int reach;
        private final Builder builder;

        Kind(int reach, Builder builder) {
            this.reach = reach;
            this.builder = builder;
        }
    }

    @FunctionalInterface
    public interface Builder {
        boolean build(World world, Random random, BlockPos origin);
    }

    private static Entry building;

    private final List<Entry> entries = new ArrayList<>();
    private int cursor;

    public DeferredStructureManager(String name) {
        super(name);
    }

    /** Call during population with a seed taken from the population random, so the build is the same every time. */
    public static void schedule(World world, Kind kind, BlockPos origin, long seed) {
        DeferredStructureManager manager = get(world);
        manager.entries.add(new Entry(kind, origin, seed));
        manager.markDirty();
        LOGGER.debug("Queued deferred {} at {}", kind, origin);
    }

    private static DeferredStructureManager get(World world) {
        MapStorage storage = world.getPerWorldStorage();
        DeferredStructureManager manager = (DeferredStructureManager) storage.getOrLoadData(DeferredStructureManager.class, NAME);
        if (manager == null) {
            manager = new DeferredStructureManager(NAME);
            storage.setData(NAME, manager);
        }
        return manager;
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.world.isRemote) {
            get(event.world).buildNextReady((WorldServer) event.world);
        }
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (building != null && !event.getWorld().isRemote) {
            LOGGER.warn("Deferred {} at {} loaded chunk {},{} outside its reach", building.kind, building.origin,
                    event.getChunk().x, event.getChunk().z);
        }
    }

    private void buildNextReady(WorldServer world) {
        int checks = Math.min(CHECKS_PER_TICK, entries.size());
        for (int i = 0; i < checks; i++) {
            if (cursor >= entries.size()) {
                cursor = 0;
            }
            Entry entry = entries.get(cursor);
            if (isReady(world, entry)) {
                entries.remove(cursor);
                markDirty();
                build(world, entry);
                return;
            }
            cursor++;
        }
    }

    private static boolean isReady(WorldServer world, Entry entry) {
        // One block past the reach covers neighbour updates from blocks written at its edge
        int reach = entry.kind.reach + 1;
        for (int chunkX = (entry.origin.getX() - reach) >> 4; chunkX <= (entry.origin.getX() + reach) >> 4; chunkX++) {
            for (int chunkZ = (entry.origin.getZ() - reach) >> 4; chunkZ <= (entry.origin.getZ() + reach) >> 4; chunkZ++) {
                Chunk chunk = world.getChunkProvider().getLoadedChunk(chunkX, chunkZ);
                if (chunk == null || !chunk.isTerrainPopulated()) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void build(WorldServer world, Entry entry) {
        boolean fallInstantly = BlockFalling.fallInstantly;
        // Sand and gravel settle at once, as they do during population, instead of turning into falling entities
        BlockFalling.fallInstantly = true;
        building = entry;
        try {
            boolean built = entry.kind.builder.build(world, new Random(entry.seed), entry.origin);
            LOGGER.debug(built ? "Built deferred {} at {}" : "Deferred {} at {} did not fit", entry.kind, entry.origin);
        } catch (RuntimeException e) {
            LOGGER.error("Deferred {} at {} failed to build", entry.kind, entry.origin, e);
        } finally {
            building = null;
            BlockFalling.fallInstantly = fallInstantly;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        entries.clear();
        NBTTagList list = nbt.getTagList("entries", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            Kind kind;
            try {
                kind = Kind.valueOf(tag.getString("kind"));
            } catch (IllegalArgumentException e) {
                LOGGER.warn("Dropping deferred structure of unknown kind {}", tag.getString("kind"));
                continue;
            }
            entries.add(new Entry(kind, BlockPos.fromLong(tag.getLong("origin")), tag.getLong("seed")));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagList list = new NBTTagList();
        for (Entry entry : entries) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("kind", entry.kind.name());
            tag.setLong("origin", entry.origin.toLong());
            tag.setLong("seed", entry.seed);
            list.appendTag(tag);
        }
        nbt.setTag("entries", list);
        return nbt;
    }

    private static final class Entry {
        final Kind kind;
        final BlockPos origin;
        final long seed;

        Entry(Kind kind, BlockPos origin, long seed) {
            this.kind = kind;
            this.origin = origin;
            this.seed = seed;
        }
    }
}
