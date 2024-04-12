package io.redspace.ironsspellbooks.block.pedestal;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.phys.Vec3;


public class PedestalRenderer implements BlockEntityRenderer<PedestalTile> {
    ItemRenderer itemRenderer;

    public PedestalRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    private static final Vec3 ITEM_POS = new Vec3(.5, 1.5, .5);

    @Override
    public void render(PedestalTile pedestalTile, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemStack heldItem = pedestalTile.getHeldItem();

        if (!heldItem.isEmpty()) {
            Player player = Minecraft.getInstance().player;
            float bob = (float) (Math.sin((player.tickCount + partialTick) * .1f) * .0875f);
            float rotation = player.tickCount * 2 + partialTick;
            renderItem(heldItem, ITEM_POS.add(0, bob, 0), rotation, pedestalTile, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
        }

    }

    private void renderItem(ItemStack itemStack, Vec3 offset, float yRot, PedestalTile pedestalTile, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        //renderId seems to be some kind of uuid/salt
        int renderId = (int) pedestalTile.getBlockPos().asLong();
        //BakedModel model = itemRenderer.getModel(itemStack, null, null, renderId);

        poseStack.translate(offset.x, offset.y, offset.z);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(yRot));
        if(itemStack.getItem() instanceof SwordItem || itemStack.getItem() instanceof DiggerItem){
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(-45));

        }
        //poseStack.mulPose(Vector3f.ZP.rotationDegrees(yRot));

        //poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
        poseStack.scale(0.65f, 0.65f, 0.65f);

        itemRenderer.renderStatic(itemStack, ItemTransforms.TransformType.FIXED, LevelRenderer.getLightColor(pedestalTile.getLevel(), pedestalTile.getBlockPos()), packedOverlay, poseStack, bufferSource, renderId);
        poseStack.popPose();
    }

}
