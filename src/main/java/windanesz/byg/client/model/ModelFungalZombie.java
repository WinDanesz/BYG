package windanesz.byg.client.model;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelZombie;

// Blockbench model of the fungal zombie (glowshrooms growing out of the head, sleeves on the arms).
// Extends the vanilla zombie model and only swaps out its parts, so the walk cycle, head tracking,
// raised arms, held items and armor layers all keep working.
public class ModelFungalZombie extends ModelZombie {

    public ModelFungalZombie() {
        super();
        this.textureWidth = 64;
        this.textureHeight = 64;

        this.bipedHead = new ModelRenderer(this);
        this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedHead.cubeList.add(new ModelBox(this.bipedHead, 0, 0, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F, false));
        this.bipedHead.cubeList.add(new ModelBox(this.bipedHead, 0, 32, 1.0F, -9.0F, -5.0F, 4, 8, 2, 0.0F, false));
        this.bipedHead.cubeList.add(new ModelBox(this.bipedHead, 0, 32, -5.0F, -9.0F, -5.0F, 4, 8, 2, 0.0F, true));
        this.bipedHead.cubeList.add(new ModelBox(this.bipedHead, 12, 32, -7.0F, -11.0F, -5.0F, 6, 10, 0, -0.001F, true));
        this.bipedHead.cubeList.add(new ModelBox(this.bipedHead, 12, 32, 1.0F, -11.0F, -5.0F, 6, 10, 0, -0.001F, false));
        this.addGlowshroom(-2.0F, -10.0F, -2.0F);
        this.addGlowshroom(2.0F, -12.0F, 1.0F);

        this.bipedHeadwear = new ModelRenderer(this);
        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedHeadwear.cubeList.add(new ModelBox(this.bipedHeadwear, 32, 0, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F, false));

        this.bipedBody = new ModelRenderer(this);
        this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedBody.cubeList.add(new ModelBox(this.bipedBody, 16, 16, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F, false));

        this.bipedLeftArm = new ModelRenderer(this);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.bipedLeftArm.cubeList.add(new ModelBox(this.bipedLeftArm, 40, 16, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F, true));
        this.bipedLeftArm.cubeList.add(new ModelBox(this.bipedLeftArm, 40, 32, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.5F, true));

        this.bipedRightArm = new ModelRenderer(this);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.bipedRightArm.cubeList.add(new ModelBox(this.bipedRightArm, 40, 16, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F, false));
        this.bipedRightArm.cubeList.add(new ModelBox(this.bipedRightArm, 40, 32, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.5F, false));

        this.bipedLeftLeg = new ModelRenderer(this);
        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.bipedLeftLeg.cubeList.add(new ModelBox(this.bipedLeftLeg, 0, 16, -1.9F, 0.0F, -2.0F, 4, 12, 4, 0.0F, true));

        this.bipedRightLeg = new ModelRenderer(this);
        this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
        this.bipedRightLeg.cubeList.add(new ModelBox(this.bipedRightLeg, 0, 16, -2.1F, 0.0F, -2.0F, 4, 12, 4, 0.0F, false));
    }

    // Two flat 8x8 planes crossed at +-45 degrees, parented to the head so they follow its rotation.
    private void addGlowshroom(float x, float y, float z) {
        for (float angle : new float[]{0.7854F, -0.7854F}) {
            ModelRenderer plane = new ModelRenderer(this);
            plane.setRotationPoint(x, y, z);
            plane.rotateAngleY = angle;
            plane.cubeList.add(new ModelBox(plane, 2, 51, -4.0F, -4.0F, 0.0F, 8, 8, 0, 0.0F, false));
            this.bipedHead.addChild(plane);
        }
    }
}
