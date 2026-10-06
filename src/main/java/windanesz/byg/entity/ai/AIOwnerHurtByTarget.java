package windanesz.byg.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.ai.EntityAITarget;

/**
 * Makes an owned creature turn on whatever last hurt its owner. Vanilla's
 * {@link net.minecraft.entity.ai.EntityAIOwnerHurtByTarget} only accepts tameable animals, this works for any
 * creature that is an {@link IEntityOwnable}. The owner itself and anything with the same owner is never picked,
 * that is handled by {@link EntityAITarget#isSuitableTarget}.
 */
public class AIOwnerHurtByTarget extends EntityAITarget {
    private final IEntityOwnable ownable;
    private EntityLivingBase attacker;
    /** Revenge timer of the last attack that was answered, so each attack is only reacted to once. */
    private int timestamp;

    public <T extends EntityCreature & IEntityOwnable> AIOwnerHurtByTarget(T creature) {
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
        this.attacker = livingOwner.getRevengeTarget();
        return livingOwner.getRevengeTimer() != this.timestamp && this.isSuitableTarget(this.attacker, false);
    }

    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.attacker);
        Entity owner = this.ownable.getOwner();
        if (owner instanceof EntityLivingBase) {
            this.timestamp = ((EntityLivingBase) owner).getRevengeTimer();
        }
        super.startExecuting();
    }
}
