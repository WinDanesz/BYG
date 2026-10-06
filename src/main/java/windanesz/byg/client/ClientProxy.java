package windanesz.byg.client;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import windanesz.byg.entity.EntityCrystalCrawler;
import windanesz.byg.proxy.IProxyBYG;

public class ClientProxy implements IProxyBYG {
    @Override
    public void init(FMLInitializationEvent event) {
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        windanesz.byg.entity.EntityFungalZombie.preInit(event);
        windanesz.byg.entity.EntityKiwiBird.preInit(event);
        windanesz.byg.entity.EntityKiwiEgg.preInit(event);
        windanesz.byg.entity.EntityMudBall.preInit(event);
        windanesz.byg.entity.EntityCrystalCrawler.preInit(event);
        RenderingRegistry.registerEntityRenderingHandler(EntityCrystalCrawler.class, RenderCrystalCrawler::new);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
    }

    @Override
    public void serverLoad(FMLServerStartingEvent event) {
    }
}
