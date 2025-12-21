package klism.wileyborder;

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

    public static void register() {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
            var client = MinecraftClient.getInstance();
            if (client.world == null) return;

            WileyBorderConfig cfg = ConfigManager.get();
            if (!cfg.enabled) return;

            MatrixStack matrices = context.matrixStack();
            Vec3d cam = context.camera().getPos();

            float a = ((cfg.argb >> 24) & 0xFF) / 255.0f;
            float r = ((cfg.argb >> 16) & 0xFF) / 255.0f;
            float g = ((cfg.argb >> 8) & 0xFF) / 255.0f;
            float b = (cfg.argb & 0xFF) / 255.0f;

            double y1World = cam.y - RADIUS;
            double y2World = cam.y + RADIUS;

            double x1, x2, y1, y2, z1, z2;

            y1 = y1World - cam.y;
            y2 = y2World - cam.y;

            if (cfg.axis == Axis.X) {
                double x = cfg.coordinate;
                x1 = (x - THICKNESS / 2.0) - cam.x;
                x2 = (x + THICKNESS / 2.0) - cam.x;

                z1 = (cam.z - RADIUS) - cam.z;
                z2 = (cam.z + RADIUS) - cam.z;
            } else {
                double z = cfg.coordinate;
                z1 = (z - THICKNESS / 2.0) - cam.z;
                z2 = (z + THICKNESS / 2.0) - cam.z;

                x1 = (cam.x - RADIUS) - cam.x;
                x2 = (cam.x + RADIUS) - cam.x;
            }

            VertexConsumer vc = context.consumers().getBuffer(RenderLayer.getDebugFilledBox());
            VertexRendering.drawFilledBox(matrices, vc, x1, y1, z1, x2, y2, z2, r, g, b, a);
        });
    }
}
