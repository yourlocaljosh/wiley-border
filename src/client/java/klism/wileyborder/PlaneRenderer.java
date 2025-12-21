package klism.wileyborder;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public final class PlaneRenderer {
    private static final double RADIUS = 256.0;
    private static final double THICKNESS = 0.04;

    private PlaneRenderer() {}

    private static float clamp01(float v) {
        if (v < 0f) {
            return 0f;
        }
        if (v > 1f) {
            return 1f;
        }
        return v;
    }

    public static void register() {
        WorldRenderEvents.LAST.register(context -> {
            var client = MinecraftClient.getInstance();
            if (client.world == null) return;

            WileyBorderConfig cfg = ConfigManager.get();

            MatrixStack matrices = context.matrixStack();
            Vec3d cam = context.camera().getPos();

            renderOne(context, matrices, cam, cfg.border1);
            renderOne(context, matrices, cam, cfg.border2);
            renderOne(context, matrices, cam, cfg.border3);
            renderOne(context, matrices, cam, cfg.border4);
        });
    }

    private static void renderOne(WorldRenderContext context,
                                  MatrixStack matrices,
                                  Vec3d cam,
                                  BorderConfig border) {
        if (border == null || !border.enabled) return;

        float a = clamp01(border.opacity);
        float r = ((border.rgb >> 16) & 0xFF) / 255.0f;
        float g = ((border.rgb >> 8) & 0xFF) / 255.0f;
        float b = (border.rgb & 0xFF) / 255.0f;

        double y1 = -RADIUS;
        double y2 =  RADIUS;

        double x1, x2, z1, z2;

        if (border.axis == Axis.X) {
            double x = border.coordinate;
            x1 = (x - THICKNESS / 2.0) - cam.x;
            x2 = (x + THICKNESS / 2.0) - cam.x;
            z1 = -RADIUS;
            z2 =  RADIUS;
        } else {//z
            double z = border.coordinate;
            z1 = (z - THICKNESS / 2.0) - cam.z;
            z2 = (z + THICKNESS / 2.0) - cam.z;
            x1 = -RADIUS;
            x2 =  RADIUS;
        }

        VertexConsumer vc = context.consumers().getBuffer(RenderLayer.getDebugFilledBox());
        VertexRendering.drawFilledBox(matrices, vc, x1, y1, z1, x2, y2, z2, r, g, b, a);
    }
}
