package windanesz.byg.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.registry.ModItems;

/** A thrown Kiwi Egg. Like a chicken egg, it has a chance to hatch into one or more baby kiwis where it lands. */
public class EntityKiwiEgg extends EntityThrowable {
    public EntityKiwiEgg(World world) {
        super(world);
    }

    public EntityKiwiEgg(World world, EntityLivingBase thrower) {
        super(world, thrower);
    }

    public EntityKiwiEgg(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @SideOnly(Side.CLIENT)
    public static void preInit(FMLPreInitializationEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityKiwiEgg.class, renderManager -> new RenderSnowball<>(renderManager, ModItems.kiwi_egg, Minecraft.getMinecraft().getRenderItem()));
    }

    public static void registerDispenseBehavior() {
        if (ModItems.kiwi_egg == null) {
            return;
        }
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.kiwi_egg, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(World world, IPosition position, ItemStack stack) {
                return new EntityKiwiEgg(world, position.getX(), position.getY(), position.getZ());
            }
        });
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (result.entityHit != null) {
            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), 0.0F);
        }

        if (!world.isRemote) {
            if (rand.nextInt(8) == 0) {
                int count = rand.nextInt(32) == 0 ? 4 : 1;
                for (int i = 0; i < count; i++) {
                    EntityKiwiBird kiwi = new EntityKiwiBird(world);
                    kiwi.setGrowingAge(-24000);
                    kiwi.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                    world.spawnEntity(kiwi);
                }
            }
            world.setEntityState(this, (byte) 3);
            setDead();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            int particleItemId = Item.getIdFromItem(ModItems.kiwi_egg);
            for (int i = 0; i < 8; ++i) {
                world.spawnParticle(EnumParticleTypes.ITEM_CRACK, posX, posY, posZ,
                        (rand.nextFloat() - 0.5D) * 0.08D, (rand.nextFloat() - 0.5D) * 0.08D, (rand.nextFloat() - 0.5D) * 0.08D, particleItemId);
            }
            return;
        }
        super.handleStatusUpdate(id);
    }
}
