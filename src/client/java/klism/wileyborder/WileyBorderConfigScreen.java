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

        ConfigEntryBuilder eb = builder.entryBuilder();

        addBorderCategory(builder, eb, "Border 1", cfg.border1);
        addBorderCategory(builder, eb, "Border 2", cfg.border2);
        addBorderCategory(builder, eb, "Border 3", cfg.border3);
        addBorderCategory(builder, eb, "Border 4", cfg.border4);

        return builder.build();
    }

    private static void addBorderCategory(ConfigBuilder builder, ConfigEntryBuilder eb, String title, BorderConfig border) {
        ConfigCategory cat = builder.getOrCreateCategory(Text.literal(title));

        cat.addEntry(
                eb.startBooleanToggle(Text.literal("Enabled"), border.enabled)
                        .setDefaultValue(false)
                        .setSaveConsumer(val -> border.enabled = val)
                        .build()
        );

        cat.addEntry(
                eb.startEnumSelector(Text.literal("Axis"), Axis.class, border.axis)
                        .setDefaultValue(Axis.X)
                        .setSaveConsumer(val -> border.axis = val)
                        .build()
        );

        cat.addEntry(
                eb.startIntField(Text.literal("Coords"), border.coordinate)
                        .setDefaultValue(0)
                        .setMin(Integer.MIN_VALUE)
                        .setMax(Integer.MAX_VALUE)
                        .setSaveConsumer(val -> border.coordinate = val)
                        .build()
        );

        cat.addEntry(
                eb.startColorField(Text.literal("Color"), border.argb)
                        .setDefaultValue(0x33FF0000)
                        .setAlphaMode(true)
                        .setSaveConsumer(val -> border.argb = val)
                        .build()
        );
    }
}
