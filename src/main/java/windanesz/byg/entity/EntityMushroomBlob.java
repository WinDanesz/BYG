package windanesz.byg.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** The blob of mushroom a {@link EntityFungalSkeleton} spits from its mouth. */
public class EntityMushroomBlob extends EntityThrowable {
    private static final float DAMAGE = 3.0F;

    public EntityMushroomBlob(World world) {
        super(world);
    }

    public EntityMushroomBlob(World world, EntityLivingBase thrower) {
        super(world, thrower);
    }

    public EntityMushroomBlob(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    /** The item whose icon the impact particles are made of. */
    private static Item particleItem() {
        return Item.getItemFromBlock(Blocks.BROWN_MUSHROOM);
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        Entity hit = result.entityHit;
        // fungal creatures are immune to each other's spit, so a pack does not hurt itself
        if (hit != null && !(hit instanceof EntityFungalZombie) && !(hit instanceof EntityFungalSkeleton)) {
            hit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), DAMAGE);
        }

        if (!this.world.isRemote) {
            this.world.setEntityState(this, (byte) 3);
            this.setDead();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            int particleItemId = Item.getIdFromItem(particleItem());
            for (int i = 0; i < 8; ++i) {
                this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, 0.0, 0.0, 0.0, particleItemId);
            }
            return;
        }
        super.handleStatusUpdate(id);
    }
}
