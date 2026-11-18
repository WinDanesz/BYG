package windanesz.byg.registry;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IFixableData;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import windanesz.byg.BiomesYouGo;
import windanesz.byg.blocks.BlockDirectionalLogBase;

public final class ModDataFixes {
    private static final int VERSION = 1;

    private ModDataFixes() {
    }

    public static void register() {
        ModFixs fixes = FMLCommonHandler.instance().getDataFixer().init(BiomesYouGo.MODID, VERSION);
        fixes.registerFix(FixTypes.STRUCTURE, new LogAxisFix());
    }

    // Structures saved the logs with the facing they used to have, which a vanilla-style log would silently drop and stand upright.
    // Placed blocks need no fix, as the logs read their old metadata back as the same axis.
    static final class LogAxisFix implements IFixableData {
        @Override
        public int getFixVersion() {
            return 1;
        }

        @Override
        public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
            NBTTagList palette = compound.getTagList("palette", 10);
            for (int i = 0; i < palette.tagCount(); i++) {
                NBTTagCompound entry = palette.getCompoundTagAt(i);
                NBTTagCompound properties = entry.getCompoundTag("Properties");
                if (!properties.hasKey("facing", 8)) {
                    continue;
                }
                Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(entry.getString("Name")));
                if (block instanceof BlockDirectionalLogBase) {
                    properties.setString("axis", axisOf(properties.getString("facing")));
                    properties.removeTag("facing");
                }
            }
            return compound;
        }

        static String axisOf(String facing) {
            switch (facing) {
                case "down":
                case "up":
                    return "x";
                case "west":
                case "east":
                    return "z";
                default:
                    return "y";
            }
        }
    }
}
