package windanesz.byg.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Walks an owned creature back to its owner once it has strayed too far. Vanilla's
 * {@link net.minecraft.entity.ai.EntityAIFollowOwner} only accepts tameable animals, this works for any
 * creature that is an {@link IEntityOwnable}. Unlike the vanilla one it does not sit or teleport, and it never
 * runs while the creature has an attack target, so fighting always takes over from following.
 */
public class AIFollowOwner extends EntityAIBase {
    private static final int REPATH_INTERVAL_TICKS = 10;

    private final EntityCreature creature;
    private final IEntityOwnable ownable;
    private final double speed;
    private final double startDistanceSq;
    private final double stopDistanceSq;

    private EntityLivingBase owner;
    private int repathCooldown;

    /**
     * @param startDistance starts following once the owner is further away than this many blocks
     * @param stopDistance  stops following once the owner is closer than this many blocks
     */
    public <T extends EntityCreature & IEntityOwnable> AIFollowOwner(T creature, double speed, float startDistance, float stopDistance) {
        this.creature = creature;
        this.ownable = creature;
        this.speed = speed;
        this.startDistanceSq = startDistance * startDistance;
        this.stopDistanceSq = stopDistance * stopDistance;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        if (this.creature.getAttackTarget() != null) {
            return false;
        }
        Entity owner = this.ownable.getOwner();
        if (!(owner instanceof EntityLivingBase) || !owner.isEntityAlive()) {
            return false;
        }
        if (owner instanceof EntityPlayer && ((EntityPlayer) owner).isSpectator()) {
            return false;
        }
        if (this.creature.getDistanceSq(owner) < this.startDistanceSq) {
            return false;
        }
        this.owner = (EntityLivingBase) owner;
        return true;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.creature.getAttackTarget() == null && this.owner.isEntityAlive() && !this.creature.getNavigator().noPath()
                && this.creature.getDistanceSq(this.owner) > this.stopDistanceSq;
    }

    @Override
    public void startExecuting() {
        this.repathCooldown = 0;
    }

    @Override
    public void resetTask() {
        this.owner = null;
        this.creature.getNavigator().clearPath();
    }

    @Override
    public void updateTask() {
        this.creature.getLookHelper().setLookPositionWithEntity(this.owner, 10.0F, this.creature.getVerticalFaceSpeed());
        if (--this.repathCooldown <= 0) {
            this.repathCooldown = REPATH_INTERVAL_TICKS;
            this.creature.getNavigator().tryMoveToEntityLiving(this.owner, this.speed);
        }
    }
}
