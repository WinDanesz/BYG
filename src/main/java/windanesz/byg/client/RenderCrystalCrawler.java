package windanesz.byg.client;

import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.entity.EntityCrystalCrawler;

import java.util.Map;
import java.util.WeakHashMap;

// Crystal crawler stuff
// Uses the same model as a spider, but with rotations when climbing walls and ceilings
@SideOnly(Side.CLIENT)
public class RenderCrystalCrawler extends RenderLiving<EntityCrystalCrawler> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("byg:textures/entity/crystal_crawler.png");

    private final Map<EntityCrystalCrawler, CrawlerSurfaceTransition> surfaceTransitions = new WeakHashMap<>();

    public RenderCrystalCrawler(RenderManager renderManager) {
        super(renderManager, new ModelSpider(), 0.35F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityCrystalCrawler entity) {
        return TEXTURE;
    }

    @Override
    protected void preRenderCallback(EntityCrystalCrawler entity, float partialTickTime) {
        GlStateManager.scale(0.7F, 0.7F, 0.7F);
    }

    @Override
    protected float getDeathMaxRotation(EntityCrystalCrawler entity) {
        return 180.0F;
    }

    @Override
    protected void applyRotations(EntityCrystalCrawler entity, float ageInTicks, float rotationYaw, float partialTicks) {
        if (entity.deathTime > 0) {
            super.applyRotations(entity, ageInTicks, rotationYaw, partialTicks);
            return;
        }

        EnumFacing facing = entity.getClimbFacing();
        CrawlerSurfaceTransition transition = this.surfaceTransitions.computeIfAbsent(entity, key -> new CrawlerSurfaceTransition());
        CrawlerSurfaceTransition.Pose pose = transition.sample(facing, entity.width, entity.height, entity.ticksExisted + partialTicks);
        GlStateManager.translate(pose.offsetX, pose.offsetY, pose.offsetZ);
        float axisLength = (float) Math.sqrt(pose.x * pose.x + pose.y * pose.y + pose.z * pose.z);
        // skip rotation if the axis is too small
        if (axisLength > 1.0E-6F) {
            float angle = (float) Math.toDegrees(2.0D * Math.atan2(axisLength, pose.w));
            GlStateManager.rotate(angle, pose.x / axisLength, pose.y / axisLength, pose.z / axisLength);
        }
        if (facing == EnumFacing.UP) {
            super.applyRotations(entity, ageInTicks, rotationYaw, partialTicks);
        } else {
            GlStateManager.rotate(180.0F - rotationYaw, 0.0F, 1.0F, 0.0F);
        }
    }

    static final class CrawlerSurfaceTransition {
        static final float DURATION_TICKS = 8.0F;

        private EnumFacing facing;
        private Pose from;
        private Pose to;
        private float startTime;

        Pose sample(EnumFacing facing, float width, float height, float time) {
            if (this.facing == null) {
                this.facing = facing;
                this.from = this.to = Pose.forFacing(facing, width, height);
                this.startTime = time;
            } else if (this.facing != facing) {
                // Retarget from the visible pose even if another corner interrupts the previous transition.
                this.from = this.interpolate(time);
                this.to = Pose.forFacing(facing, width, height);
                this.facing = facing;
                this.startTime = time;
            }
            return this.interpolate(time);
        }

        private Pose interpolate(float time) {
            float t = Math.max(0.0F, Math.min(1.0F, (time - this.startTime) / DURATION_TICKS));
            t = t * t * (3.0F - 2.0F * t);
            return Pose.interpolate(this.from, this.to, t);
        }

        static final class Pose {
            final float offsetX;
            final float offsetY;
            final float offsetZ;
            final float x;
            final float y;
            final float z;
            final float w;

            private Pose(float offsetX, float offsetY, float offsetZ, float x, float y, float z, float w) {
                this.offsetX = offsetX;
                this.offsetY = offsetY;
                this.offsetZ = offsetZ;
                float length = (float) Math.sqrt(x * x + y * y + z * z + w * w);
                this.x = x / length;
                this.y = y / length;
                this.z = z / length;
                this.w = w / length;
            }

            private static Pose forFacing(EnumFacing facing, float width, float height) {
                // Put the feet plane at the touching hitbox edge, with a one-pixel inset into the surface.
                float offset = facing == EnumFacing.UP ? 0.0F
                        : 1.0F / 16.0F + (facing == EnumFacing.DOWN ? height : width * 0.5F);
                float halfSqrt = (float) Math.sqrt(0.5D);
                float x = 0.0F;
                float z = 0.0F;
                float w = halfSqrt;
                switch (facing) {
                    case NORTH:
                        x = -halfSqrt;
                        break;
                    case SOUTH:
                        x = halfSqrt;
                        break;
                    case EAST:
                        z = -halfSqrt;
                        break;
                    case WEST:
                        z = halfSqrt;
                        break;
                    case DOWN:
                        x = 1.0F;
                        w = 0.0F;
                        break;
                    default:
                        w = 1.0F;
                        break;
                }
                return new Pose(-facing.getXOffset() * offset, -facing.getYOffset() * offset,
                        -facing.getZOffset() * offset, x, 0.0F, z, w);
            }

            private static Pose interpolate(Pose a, Pose b, float t) {
                // Normalized quaternion interpolation avoids Euler flips and takes the short rotation path.
                float sign = a.x * b.x + a.y * b.y + a.z * b.z + a.w * b.w < 0.0F ? -1.0F : 1.0F;
                float s = 1.0F - t;
                return new Pose(s * a.offsetX + t * b.offsetX, s * a.offsetY + t * b.offsetY,
                        s * a.offsetZ + t * b.offsetZ, s * a.x + t * sign * b.x,
                        s * a.y + t * sign * b.y, s * a.z + t * sign * b.z, s * a.w + t * sign * b.w);
            }
        }
    }
}
