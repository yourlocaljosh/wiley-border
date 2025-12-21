package klism.wileyborder;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public final class WileyBorderConfigScreen {
    private WileyBorderConfigScreen() {}

    public static Screen create(Screen parent) {
        WileyBorderConfig cfg = ConfigManager.get();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("Wiley Border"))
                .setSavingRunnable(ConfigManager::save);

        ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));
        ConfigEntryBuilder eb = builder.entryBuilder();

        general.addEntry(
                eb.startBooleanToggle(Text.literal("Enabled"), cfg.enabled)
                        .setDefaultValue(true)
                        .setSaveConsumer(val -> cfg.enabled = val)
                        .build()
        );

        general.addEntry(
                eb.startEnumSelector(Text.literal("Axis"), Axis.class, cfg.axis)
                        .setDefaultValue(Axis.X)
                        .setSaveConsumer(val -> cfg.axis = val)
                        .build()
        );

        general.addEntry(
                eb.startIntField(Text.literal("Coordinate"), cfg.coordinate)
                        .setDefaultValue(0)
                        .setMin(Integer.MIN_VALUE)
                        .setMax(Integer.MAX_VALUE)
                        .setSaveConsumer(val -> cfg.coordinate = val)
                        .build()
        );

        general.addEntry(
                eb.startColorField(Text.literal("Plane Color"), cfg.argb)
                        .setDefaultValue(0x33FF0000)
                        .setAlphaMode(true)
                        .setSaveConsumer(val -> cfg.argb = val)
                        .build()
        );

        return builder.build();
    }
}