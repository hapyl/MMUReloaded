package me.hapyl.mmu3.feature.banner;

import com.google.common.collect.Lists;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.inventory.gui.GUI;
import me.hapyl.eterna.module.inventory.gui.SlotPattern;
import me.hapyl.eterna.module.inventory.gui.SmartComponent;
import me.hapyl.eterna.module.util.ColorConverter;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.utils.PanelGUI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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
                // Jesus fuck name one person in the fucking world who asked for them to change them from being a fucking enum
                player -> new SelectGUI<>("Select Pattern", Registry.BANNER_PATTERN.iterator(), Size.FIVE) {
                    @Nonnull
                    @Override
                    protected SelectResponse of(int index, @Nonnull PatternType type) {
                        return new SelectResponse() {
                            @Nonnull
                            @Override
                            public ItemStack getItemStack(int i) {
                                return data.builder()
                                        .setPattern(type, pattern.getColor())
                                        .setName(Chat.capitalize(type.getKey().getKey()))
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
                player -> new SelectGUI<>("Select Color", DyeColor.values(), Size.TWO) {
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

    private static <E> List<E> arrayToList(E[] array) {
        return Arrays.asList(array);
    }

    private static <E> List<E> iteratorToList(Iterator<E> iterator) {
        final List<E> list = Lists.newArrayList();

        iterator.forEachRemaining(list::add);
        return list;
    }

    private abstract class SelectGUI<E> extends PanelGUI {

        private final List<E> list;

        public SelectGUI(String name, E[] iterable, Size size) {
            super(BannerEditorLayerGUI.this.player, name, size);

            this.list = List.of(iterable);
            this.updateInventory();
        }

        public SelectGUI(String name, Iterator<E> iterable, Size size) {
            super(BannerEditorLayerGUI.this.player, name, size);

            this.list = iteratorToList(iterable);
            this.updateInventory();
        }

        @Override
        public void updateInventory() {
            final SmartComponent component = newSmartComponent();

            for (int i = 0; i < list.size(); i++) {
                final E e = list.get(i);
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
