package windanesz.byg.entity;

import net.minecraft.block.Block;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.Config;
import windanesz.byg.client.model.ModelFungalSkeleton;
import windanesz.byg.registry.ModItems;

/**
 * A skeleton overgrown with mushrooms. It never carries a bow, instead it spits {@link EntityMushroomBlob}s from its mouth.
 */
public class EntityFungalSkeleton extends EntityMob implements IRangedAttackMob {
    /** How far in front of the eyes, and how far below them, the mouth is. */
    private static final double MOUTH_REACH = 0.35D;
    private static final double MOUTH_DROP = 0.15D;

    /**
     * A spit takes a moment: the head tilts back for {@link #SPIT_WIND_UP_TICKS}, snaps forward, and the blob leaves at
     * {@link #SPIT_RELEASE_TICKS} when the head is thrust all the way forward, then the head settles until
     * {@link #SPIT_TOTAL_TICKS}. The model reads these to pose the head.
     */
    public static final int SPIT_WIND_UP_TICKS = 8;
    public static final int SPIT_RELEASE_TICKS = 11;
    public static final int SPIT_TOTAL_TICKS = 18;
    /** Entity status sent to clients when a spit starts, so they can play the head animation. */
    private static final byte SPIT_STATUS = 4;
    private static final int NOT_SPITTING = Integer.MIN_VALUE;

    /** {@link #ticksExisted} when the current spit started, on either side. */
    private int spitStartTick = NOT_SPITTING;
    /** Server only: who the spit in progress is aimed at, until the blob is released. */
    private EntityLivingBase spitTarget;

    public EntityFungalSkeleton(World world) {
        super(world);
        this.setSize(0.6f, 1.99f);
        this.experienceValue = 10;
        // Swimming also lets the navigator plan paths from inside water. By default water counts as eight times as
        // costly as land, so mobs refuse to wade; at 0 they swim the direct way to their target.
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.setPathPriority(PathNodeType.WATER, 0.0F);
        this.tasks.addTask(2, new EntityAIAttackRanged(this, 1.0, 30, 60, 12.0F));
        this.tasks.addTask(3, new EntityAIRestrictSun(this));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }

    public static void init(FMLInitializationEvent event) {
        int spawnWeight = Config.getFungalSkeletonSpawnWeight();
        if (spawnWeight > 0 && Config.isWoodSetEnabled("fungal_skeleton")) {
            EntitySpawnHelper.addSpawnIfBiomesPresent(EntityFungalSkeleton.class, spawnWeight, 2, 3, EnumCreatureType.MONSTER, new ResourceLocation("byg:byg_fungal_jungle"));
        }
    }

    @SideOnly(Side.CLIENT)
    public static void preInit(FMLPreInitializationEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityFungalSkeleton.class, renderManager -> {
            RenderBiped customRender = new RenderBiped(renderManager, new ModelFungalSkeleton(), 0.5f) {

                protected ResourceLocation getEntityTexture(Entity entity) {
                    return new ResourceLocation("byg:textures/entity/fungal_skeleton.png");
                }
            };
            customRender.addLayer((LayerRenderer) new LayerBipedArmor((RenderLivingBase) customRender) {

                protected void initArmor() {
                    this.modelLeggings = new ModelZombie(0.5f, true);
                    this.modelArmor = new ModelZombie(1.0f, true);
                }
            });
            return customRender;
        });
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(24.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        // Only starts the spit, the blob itself leaves in onLivingUpdate once the head has been thrown forward.
        if (this.spitTarget != null) {
            return;
        }
        this.spitTarget = target;
        this.spitStartTick = this.ticksExisted;
        this.world.setEntityState(this, SPIT_STATUS);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!this.world.isRemote && this.spitTarget != null && this.ticksExisted - this.spitStartTick >= SPIT_RELEASE_TICKS) {
            EntityLivingBase target = this.spitTarget;
            this.spitTarget = null;
            if (this.isEntityAlive() && target.isEntityAlive()) {
                this.spitBlobAt(target);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == SPIT_STATUS) {
            this.spitStartTick = this.ticksExisted;
            return;
        }
        super.handleStatusUpdate(id);
    }

    /**
     * How many ticks into the current spit it is, or -1 when it is not spitting. For animating the head.
     *
     * @param ageInTicks {@link #ticksExisted} plus the partial tick, as the renderer hands it to the model
     */
    public float getSpitTicks(float ageInTicks) {
        if (this.spitStartTick == NOT_SPITTING) {
            return -1.0F;
        }
        float elapsed = ageInTicks - this.spitStartTick;
        return elapsed >= 0.0F && elapsed <= SPIT_TOTAL_TICKS ? elapsed : -1.0F;
    }

    private void spitBlobAt(EntityLivingBase target) {
        EntityMushroomBlob blob = new EntityMushroomBlob(this.world, this);
        // The blob leaves from the mouth, a little below the eyes and out past the front of the head.
        Vec3d look = this.getLook(1.0F);
        blob.setPosition(this.posX + look.x * MOUTH_REACH,
                this.posY + this.getEyeHeight() - MOUTH_DROP + look.y * MOUTH_REACH,
                this.posZ + look.z * MOUTH_REACH);

        // Same aiming as the snow golem: at the target's chest, lobbed a little higher the further away it is.
        double dx = target.posX - this.posX;
        double dy = target.posY + target.getEyeHeight() - 1.1D - blob.posY;
        double dz = target.posZ - this.posZ;
        float arc = MathHelper.sqrt(dx * dx + dz * dz) * 0.2F;
        blob.shoot(dx, dy + arc, dz, 1.6F, 14 - this.world.getDifficulty().getId() * 4);

        this.playSound(SoundEvents.ENTITY_LLAMA_SPIT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.spawnEntity(blob);
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {
        // it has no bow to draw, nothing to animate
    }

    @Override
    protected Item getDropItem() {
        return Items.BONE;
    }

    @Override
    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
        super.dropFewItems(wasRecentlyHit, lootingModifier);
        // a rare glowshroom, about 2.5% and better with Looting, only when a player did the killing
        if (wasRecentlyHit && ModItems.purple_glowshroom != null && this.rand.nextInt(200) - lootingModifier < 5) {
            this.dropItem(ModItems.purple_glowshroom, 1);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ENTITY_SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SKELETON_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block block) {
        this.playSound(SoundEvents.ENTITY_SKELETON_STEP, 0.15F, 1.0F);
    }
}
