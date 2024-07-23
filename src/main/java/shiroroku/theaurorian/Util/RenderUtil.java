package shiroroku.theaurorian.Util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RenderUtil {

    public static void renderItem(ItemRenderer itemRenderer, PoseStack pPoseStack, Item item, int x, int y) {
        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();
        modelViewStack.mulPoseMatrix(pPoseStack.last().pose());
        RenderSystem.enableDepthTest();
        itemRenderer.renderAndDecorateFakeItem(new ItemStack(item), -8, -8);
        modelViewStack.popPose();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.enableBlend();
    }

    public static void blit(PoseStack pPoseStack, ResourceLocation atlas, int x, int y, float pUOffset, float pVOffset, int pWidth, int pHeight, int pTextureWidth, int pTextureHeight) {
        RenderSystem.setShaderTexture(0, atlas);
        GuiComponent.blit(pPoseStack, x, y, pUOffset, pVOffset, pWidth, pHeight, pTextureWidth, pTextureHeight);
    }

    public static void blitRepeating(ResourceLocation atlas, int x, int y, int w, int h, float u, float v) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        RenderSystem.setShaderTexture(0, atlas);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(x, y, 0.0D).uv(u, v).endVertex();
        bufferbuilder.vertex(x, y + h, 0.0D).uv(u, v + 1).endVertex();
        bufferbuilder.vertex(x + w, y + h, 0.0D).uv(u + 1, v + 1).endVertex();
        bufferbuilder.vertex(x + w, y, 0.0D).uv(u + 1, v).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
    }

    public static boolean isMouseOver(int pX, int pY, int pWidth, int pHeight, double pMouseX, double pMouseY) {
        return pMouseX >= (pX - 1) && pMouseX < (pX + pWidth + 1) && pMouseY >= (pY - 1) && pMouseY < (pY + pHeight + 1);
    }
}
