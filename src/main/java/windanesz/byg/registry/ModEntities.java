package windanesz.byg.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import windanesz.byg.Config;
import windanesz.byg.entity.EntityCrystalCrawler;
import windanesz.byg.entity.EntityFungalSkeleton;
import windanesz.byg.entity.EntityFungalZombie;
import windanesz.byg.entity.EntityKiwiBird;
import windanesz.byg.entity.EntityKiwiEgg;
import windanesz.byg.entity.EntityMudBall;
import windanesz.byg.entity.EntityMushroomBlob;

@Mod.EventBusSubscriber
public final class ModEntities {
    private ModEntities() {
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<EntityEntry> event) {
        java.util.List<EntityEntry> entries = new java.util.ArrayList<>();
        entries.add(
                EntityEntryBuilder.create().entity(EntityMudBall.class).id(new ResourceLocation("byg", "mudball"), 61).name("mudball").tracker(64, 10, true).build());
        if (Config.isWoodSetEnabled("fungal_zombie")) entries.add(
                EntityEntryBuilder.create().entity(EntityFungalZombie.class).id(new ResourceLocation("byg", "fungalzombie"), 48).name("fungalzombie").tracker(64, 1, true).egg(-10053376, -8578791).build());
        if (Config.isWoodSetEnabled("fungal_skeleton")) {
            entries.add(
                    EntityEntryBuilder.create().entity(EntityFungalSkeleton.class).id(new ResourceLocation("byg", "fungalskeleton"), 49).name("fungalskeleton").tracker(64, 1, true).egg(0xC1BDAA, 0x5B4636).build());
            entries.add(
                    EntityEntryBuilder.create().entity(EntityMushroomBlob.class).id(new ResourceLocation("byg", "mushroomblob"), 70).name("mushroomblob").tracker(64, 10, true).build());
        }
        if (Config.isWoodSetEnabled("kiwi_bird")) entries.add(
                EntityEntryBuilder.create().entity(EntityKiwiEgg.class).id(new ResourceLocation("byg", "kiwiegg"), 62).name("kiwiegg").tracker(64, 10, true).build());
        if (Config.isWoodSetEnabled("kiwi_bird")) entries.add(
                EntityEntryBuilder.create().entity(EntityKiwiBird.class).id(new ResourceLocation("byg", "kiwibird"), 56).name("kiwibird").tracker(64, 1, true).egg(-13624819, -2172876).build());
        if (Config.isWoodSetEnabled("crystal_crawler")) entries.add(
                EntityEntryBuilder.create().entity(EntityCrystalCrawler.class).id(new ResourceLocation("byg", "crystalcrawler"), 52).name("crystalcrawler").tracker(64, 1, true).egg(-10734825, -15066598).build());
        event.getRegistry().registerAll(entries.toArray(new EntityEntry[0]));
    }
}
