package windanesz.byg.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.ai.EntityAITarget;

/**
 * Makes an owned creature join in on whatever its owner last attacked. Vanilla's
 * {@link net.minecraft.entity.ai.EntityAIOwnerHurtTarget} only accepts tameable animals, this works for any
 * creature that is an {@link IEntityOwnable}. The owner itself and anything with the same owner is never picked,
 * that is handled by {@link EntityAITarget#isSuitableTarget}.
 */
public class AIOwnerHurtTarget extends EntityAITarget {
    private final IEntityOwnable ownable;
    private EntityLivingBase victim;
    /** Time of the last owner attack that was joined, so each attack is only reacted to once. */
    private int timestamp;

    public <T extends EntityCreature & IEntityOwnable> AIOwnerHurtTarget(T creature) {
        super(creature, false);
        this.ownable = creature;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        Entity owner = this.ownable.getOwner();
        if (!(owner instanceof EntityLivingBase)) {
            return false;
        }
        EntityLivingBase livingOwner = (EntityLivingBase) owner;
        this.victim = livingOwner.getLastAttackedEntity();
        return livingOwner.getLastAttackedEntityTime() != this.timestamp && this.isSuitableTarget(this.victim, false);
    }

    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.victim);
        Entity owner = this.ownable.getOwner();
        if (owner instanceof EntityLivingBase) {
            this.timestamp = ((EntityLivingBase) owner).getLastAttackedEntityTime();
        }
        super.startExecuting();
    }
}
