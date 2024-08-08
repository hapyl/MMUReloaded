package me.hapyl.mmu3.feature.banner;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.utils.PanelGUI;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.inventory.gui.GUI;
import me.hapyl.eterna.module.inventory.gui.SlotPattern;
import me.hapyl.eterna.module.inventory.gui.SmartComponent;
import me.hapyl.eterna.module.util.ColorConverter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class BannerEditorLayerGUI extends PanelGUI {

    private static final int ADD_INDEX = -1;

    private final int index;
    private final int page;

    private final BannerData data;
    private final MutablePattern pattern;

    public BannerEditorLayerGUI(Player player, BannerEditorGUI gui) {
        this(player, ADD_INDEX, gui);
    }

    public BannerEditorLayerGUI(Player player, int index, BannerEditorGUI gui) {
        super(player, GUI.menuArrowSplit(
                "Banner Editor",
                (index == ADD_INDEX ? "Add Layer" : "Edit Layer " + (index + 1))
        ), Size.THREE);

        this.index = index;
        this.page = gui.page;
        this.data = Main.getRegistry().bannerEditor.getOrCreate(player);
        this.pattern = MutablePattern.of(data.getPattern(index));

        updateInventory();
    }

    @Override
    public void updateInventory() {
        setItem(
                12,
                data.builder()
                        .setPattern(pattern.asPattern())
                        .setName("Pattern: " + pattern.getPatternName())
                        .addLore()
                        .addLore("&eClick to change")
                        .build(),
                player -> new SelectGUI<>("Select Pattern", PatternType.class, Size.FIVE) {
                    @Nonnull
                    @Override
                    protected SelectResponse of(int index, @Nonnull PatternType type) {
                        return new SelectResponse() {
                            @Nonnull
                            @Override
                            public ItemStack getItemStack(int i) {
                                return data.builder()
                                        .setPattern(type, pattern.getColor())
                                        .setName(Chat.capitalize(type))
                                        .build();
                            }

                            @Override
                            public void onClick(@Nonnull Player player) {
                                pattern.setPattern(type);
                            }
                        };
                    }
                }
        );

        setItem(
                14,
                new ItemBuilder(pattern.getColorAsItem())
                        .setName("Color: " + pattern.getColorName())
                        .addLore()
                        .addLore("&eClick to change")
                        .asIcon(),
                player -> new SelectGUI<>("Select Color", DyeColor.class, Size.TWO) {
                    @Nonnull
                    @Override
                    protected SelectResponse of(int index, @Nonnull DyeColor color) {
                        return new SelectResponse() {
                            @Nonnull
                            @Override
                            public ItemStack getItemStack(int i) {
                                final ChatColor chatColor = ColorConverter.DYE_COLOR.toChatColor(color);
                                final String nameColored = chatColor + Chat.capitalize(color);

                                return new ItemBuilder(MutablePattern.colorToMaterial(color))
                                        .setName(nameColored)
                                        .addLore()
                                        .addLore("&eClick to select")
                                        .build();
                            }

                            @Override
                            public void onClick(@Nonnull Player player) {
                                pattern.setColor(color);
                            }
                        };
                    }
                }
        );

        // Set buttons
        setPanelItem(2, new ItemBuilder(Material.RED_GLAZED_TERRACOTTA)
                .setName("&cCancel")
                .addLore()
                .addLore("&eClick to cancel")
                .asIcon(), player -> new BannerEditorGUI(player, page)
        );

        setPanelItem(6, new ItemBuilder(Material.GREEN_GLAZED_TERRACOTTA)
                .setName("&aConfirm")
                .addLore()
                .addLore("&eClick to confirm")
                .asIcon(), player -> {
            if (index == ADD_INDEX) {
                data.addPattern(pattern.asPattern());
            }
            else {
                data.setPattern(index, pattern.asPattern());
            }
            new BannerEditorGUI(player, page);
        });

        openInventory();
    }

    private interface SelectResponse {

        @Nonnull
        ItemStack getItemStack(int i);

        void onClick(@Nonnull Player player);

    }

    private abstract class SelectGUI<E extends Enum<E>> extends PanelGUI {

        private final Class<E> clazz;

        public SelectGUI(String name, Class<E> clazz, Size size) {
            super(BannerEditorLayerGUI.this.player, name, size);

            this.clazz = clazz;
            this.updateInventory();
        }

        @Override
        public void updateInventory() {
            final E[] enums = clazz.getEnumConstants();
            final SmartComponent component = newSmartComponent();

            for (int i = 0; i < enums.length; i++) {
                final E e = enums[i];
                final SelectResponse response = of(i, e);

                component.add(response.getItemStack(i), player -> {
                    response.onClick(player);

                    // Update parent inventory right away
                    BannerEditorLayerGUI.this.updateInventory();
                });
            }

            component.apply(this, SlotPattern.CHUNKY, 0);
            this.openInventory();
        }

        @Nonnull
        protected abstract SelectResponse of(int index, @Nonnull E e);

    }

}
