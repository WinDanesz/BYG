package windanesz.byg.entity;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.Config;
import windanesz.byg.client.model.ModelFungalZombie;
import windanesz.byg.entity.ai.AIFollowOwner;
import windanesz.byg.entity.ai.AIOwnerHurtByTarget;
import windanesz.byg.entity.ai.AIOwnerHurtTarget;
import windanesz.byg.registry.ModItems;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityFungalZombie extends EntityMob implements IEntityOwnable {

    // null for naturally spawned zombies
    private UUID ownerId;

    public EntityFungalZombie(World world) {
        super(world);
        this.setSize(0.6f, 1.95f);
        this.experienceValue = 10;
        this.isImmuneToFire = false;
        this.setNoAI(false);
        // Swimming also lets the navigator plan paths from inside water. By default water counts as eight times as
        // costly as land, so mobs refuse to wade; at 0 they swim the direct way to their target.
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.setPathPriority(PathNodeType.WATER, 0.0F);
        // Priority 0 so it can interrupt the wander task, it never runs while there is an attack target.
        this.tasks.addTask(0, new AIFollowOwner(this, 1.0, 10.0F, 2.0F));
        this.tasks.addTask(1, new EntityAIWander(this, 1.0));
        this.tasks.addTask(2, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new AIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new AIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true, true));
        this.targetTasks.addTask(4, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.tasks.addTask(5, new EntityAIAttackMelee(this, 1.0, true));
        this.tasks.addTask(6, new EntityAIRestrictSun(this));
        this.tasks.addTask(7, new EntityAIBreakDoor(this));
    }

    public static void init(FMLInitializationEvent event) {
        int spawnWeight = Config.getFungalZombieSpawnWeight();
        if (spawnWeight > 0 && Config.isWoodSetEnabled("fungal_zombie")) {
            EntitySpawnHelper.addSpawnIfBiomesPresent(EntityFungalZombie.class, spawnWeight, 3, 4, EnumCreatureType.MONSTER, new ResourceLocation("byg:byg_fungal_jungle"));
        }
    }

    @SideOnly(Side.CLIENT)
    public static void preInit(FMLPreInitializationEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityFungalZombie.class, renderManager -> {
            RenderBiped customRender = new RenderBiped(renderManager, new ModelFungalZombie(), 0.5f) {

                protected ResourceLocation getEntityTexture(Entity entity) {
                    return new ResourceLocation("byg:textures/entity/fungal_zombie.png");
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

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    @Override
    public boolean attackEntityAsMob(Entity target) {
        boolean hit = super.attackEntityAsMob(target);
        if (hit && target instanceof EntityLivingBase) {
            int duration = Config.getFungalZombiePoisonDuration();
            if (duration > 0) {
                // creatures that cannot be poisoned, such as the undead, simply refuse the effect
                ((EntityLivingBase) target).addPotionEffect(new PotionEffect(MobEffects.POISON, duration, Config.getFungalZombiePoisonAmplifier()));
            }
        }
        return hit;
    }

    @Nullable
    @Override
    public UUID getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(@Nullable UUID ownerId) {
        this.ownerId = ownerId;
    }

    @Nullable
    @Override
    public Entity getOwner() {
        if (this.ownerId == null) {
            return null;
        }
        EntityPlayer player = this.world.getPlayerEntityByUUID(this.ownerId);
        if (player != null) {
            return player;
        }
        // the owner may not be a player, only the server can look those up by UUID
        return this.world instanceof WorldServer ? ((WorldServer) this.world).getEntityFromUuid(this.ownerId) : null;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        if (this.ownerId != null) {
            compound.setUniqueId("Owner", this.ownerId);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.ownerId = compound.hasUniqueId("Owner") ? compound.getUniqueId("Owner") : null;
    }

    protected Item getDropItem() {
        return Items.ROTTEN_FLESH;
    }

    public SoundEvent getAmbientSound() {
        return net.minecraft.init.SoundEvents.ENTITY_HUSK_AMBIENT;
    }

    public SoundEvent getHurtSound(DamageSource ds) {
        return net.minecraft.init.SoundEvents.ENTITY_ZOMBIE_HURT;
    }

    public SoundEvent getDeathSound() {
        return net.minecraft.init.SoundEvents.ENTITY_HUSK_DEATH;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        if (this.getEntityAttribute(SharedMonsterAttributes.ARMOR) != null) {
            this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(0.0);
        }
        if (this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED) != null) {
            this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2);
        }
        if (this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH) != null) {
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(35.0);
        }
        if (this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE) != null) {
            this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0);
        }
    }

    protected void dropRareDrop(int par1) {
        if (ModItems.green_glowshroom != null) {
            this.dropItem(ModItems.green_glowshroom, 1);
        }
    }

}


