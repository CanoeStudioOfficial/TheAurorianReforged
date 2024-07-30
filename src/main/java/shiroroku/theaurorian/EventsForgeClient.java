package shiroroku.theaurorian;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import shiroroku.theaurorian.Items.Loot.UmbraPickaxe;
import shiroroku.theaurorian.Registry.ItemRegistry;

@Mod.EventBusSubscriber(modid = TheAurorian.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class EventsForgeClient {

    /**
     * Handles the umbra pickaxe block outline colors
     */
    @SubscribeEvent
    public static void onRenderOutline(RenderHighlightEvent.Block event) {
        if (!Minecraft.getInstance().player.getMainHandItem().is(ItemRegistry.umbra_pickaxe.get())) {
            return;
        }

        BlockPos pPos = event.getTarget().getBlockPos();
        if (UmbraPickaxe.getSelectedBlock(Minecraft.getInstance().player.getMainHandItem()) != Minecraft.getInstance().level.getBlockState(pPos).getBlock()) {
            return;
        }

        float colorWave = 0.75f * Math.min(1, Math.max(0, 0.5f * (float) (1 + Math.sin(((double) System.currentTimeMillis()) / 200))));
        double pX = (double) pPos.getX() - event.getCamera().getPosition().x;
        double pY = (double) pPos.getY() - event.getCamera().getPosition().y;
        double pZ = (double) pPos.getZ() - event.getCamera().getPosition().z;
        event.getPoseStack().pushPose();
        PoseStack.Pose last = event.getPoseStack().last();
        VertexConsumer consumer = event.getMultiBufferSource().getBuffer(RenderType.lines());
        VoxelShape shape = Minecraft.getInstance().level.getBlockState(event.getTarget().getBlockPos()).getShape(Minecraft.getInstance().level, pPos, CollisionContext.of(event.getCamera().getEntity()));
        shape.forAllEdges((x, y, z, x1, y1, z1) -> {
            float dx = (float) (x1 - x);
            float dy = (float) (y1 - y);
            float dz = (float) (z1 - z);
            float f3 = Mth.sqrt(dx * dx + dy * dy + dz * dz);
            dx /= f3;
            dy /= f3;
            dz /= f3;
            consumer.vertex(last.pose(), (float) (x + pX), (float) (y + pY), (float) (z + pZ)).color(colorWave, colorWave, colorWave, 0.5F).normal(last.normal(), dx, dy, dz).endVertex();
            consumer.vertex(last.pose(), (float) (x1 + pX), (float) (y1 + pY), (float) (z1 + pZ)).color(colorWave, colorWave, colorWave, 0.5F).normal(last.normal(), dx, dy, dz).endVertex();
        });
        event.getPoseStack().popPose();
        event.setCanceled(true);
    }
}
