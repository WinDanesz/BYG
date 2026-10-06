package windanesz.byg.init;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.relauncher.Side;
import windanesz.byg.BiomesYouGo;
import windanesz.byg.Config;
import windanesz.byg.blocks.BlockCrate;
import windanesz.byg.blocks.BlockNetherFurnaceLit;
import windanesz.byg.blocks.BlockSpringwater;
import windanesz.byg.entity.EntityFungalZombie;
import windanesz.byg.entity.EntityKiwiBird;
import windanesz.byg.registry.ModBiomes;
import windanesz.byg.registry.ModBrewingRecipes;
import windanesz.byg.registry.SmeltingRecipes;

public final class BygInitialization {
    private static int messageID = 0;

    private BygInitialization() {
    }

    public static void preInit(FMLPreInitializationEvent event) {
        Config.init();

        BlockSpringwater.preInit(event);
    }

    public static void init(FMLInitializationEvent event) {
        ModBiomes.init();
        BlockCrate.init(event);
        EntityFungalZombie.init(event);
        EntityKiwiBird.init(event);
        windanesz.byg.entity.EntityKiwiEgg.registerDispenseBehavior();
        BlockNetherFurnaceLit.init(event);
        ModBrewingRecipes.init();
        SmeltingRecipes.init();
    }

    public static <T extends IMessage, V extends IMessage> void addNetworkMessage(Class<? extends IMessageHandler<T, V>> handler, Class<T> messageClass, Side... sides) {
        for (Side side : sides) {
            BiomesYouGo.PACKET_HANDLER.registerMessage(handler, messageClass, messageID, side);
        }
        ++messageID;
    }
}

