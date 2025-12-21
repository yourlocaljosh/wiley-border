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

    private static final double THICKNESS = 0.02;

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

            int minY = client.world.getBottomY();
            int maxY = client.world.getTopY();

            double x1, x2, y1, y2, z1, z2;

            y1 = minY;
            y2 = maxY;

            if (cfg.axis == Axis.X) {
                double x = cfg.coordinate;
                x1 = x - THICKNESS / 2.0;
                x2 = x + THICKNESS / 2.0;
                z1 = cam.z - RADIUS;
                z2 = cam.z + RADIUS;
            } else {
                double z = cfg.coordinate;
                z1 = z - THICKNESS / 2.0;
                z2 = z + THICKNESS / 2.0;
                x1 = cam.x - RADIUS;
                x2 = cam.x + RADIUS;
            }

            x1 -= cam.x; x2 -= cam.x;
            z1 -= cam.z; z2 -= cam.z;
            y1 -= cam.y; y2 -= cam.y;

            VertexConsumer vc = context.consumers().getBuffer(RenderLayer.getDebugFilledBox());
            VertexRendering.drawFilledBox(matrices, vc, x1, y1, z1, x2, y2, z2, r, g, b, a);
        });
    }
}
