package windanesz.byg.client;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.entity.EntityMushroomBlob;

// Draws the fungal skeleton's blob as a flat sprite that always faces the camera and tumbles in flight,
// the same way vanilla draws fireballs, but from its own texture instead of an item.
@SideOnly(Side.CLIENT)
public class RenderMushroomBlob extends Render<EntityMushroomBlob> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("byg:textures/entity/projectile/mushroom_blob.png");
    /** Half the width of the sprite in blocks, so it is half a block across. */
    private static final float HALF_SIZE = 0.25F;
    private static final float SPIN_DEGREES_PER_TICK = 24.0F;

    public RenderMushroomBlob(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityMushroomBlob entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        this.bindEntityTexture(entity);
        // the entity's position is the bottom of its box, draw around the middle of it
        GlStateManager.translate((float) x, (float) y + entity.height / 2.0F, (float) z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate((entity.ticksExisted + partialTicks) * SPIN_DEGREES_PER_TICK, 0.0F, 0.0F, 1.0F);

        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
        buffer.pos(-HALF_SIZE, -HALF_SIZE, 0.0D).tex(0.0D, 1.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(HALF_SIZE, -HALF_SIZE, 0.0D).tex(1.0D, 1.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(HALF_SIZE, HALF_SIZE, 0.0D).tex(1.0D, 0.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(-HALF_SIZE, HALF_SIZE, 0.0D).tex(0.0D, 0.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
        tessellator.draw();

        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityMushroomBlob entity) {
        return TEXTURE;
    }
}
