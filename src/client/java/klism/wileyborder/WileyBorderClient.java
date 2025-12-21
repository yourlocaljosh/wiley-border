package klism.wileyborder;

import net.fabricmc.api.ClientModInitializer;

public final class WileyBorderClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ConfigManager.load();
		PlaneRenderer.register();
	}
}