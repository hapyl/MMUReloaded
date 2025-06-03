package me.hapyl.mmu3.feature.banner;

import me.hapyl.eterna.module.chat.LazyEvent;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.inventory.gui.PlayerPageGUI;
import me.hapyl.eterna.module.inventory.gui.StrictAction;
import me.hapyl.eterna.module.player.PlayerLib;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.util.PanelGUI;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import javax.annotation.Nonnull;

public class BannerEditorGUI extends PanelGUI {

    private static final int[] SLOTS = new int[] {
            28, 29, 30, 31, 32, 33, 34
    };

    private static final int ITEMS_PER_PAGE = SLOTS.length;
    private static final int MAGIC_MOVE_INDEX = -1;

    private final BannerData data;
    protected int page;
    private int moveIndex;

    public BannerEditorGUI(Player player) {
        this(player, 1);
    }

    public BannerEditorGUI(Player player, int page) {
        super(player, "Banner Editor", Size.FIVE);

        this.data = Main.getRegistry().bannerEditor.getOrCreate(player);
        this.page = page;
        this.moveIndex = MAGIC_MOVE_INDEX;

        openInventory();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        fillRow(0, PANEL_ITEM);

        setItem(
                13,
                data.currentItem()
                        .addLore("&8&oThis is preview of the banner!")
                        .addLore()
                        .addLore("&8◦ &eLeft-Click to get the item")
                        .addLore("&8◦ &6Right-Click to change base color")
                        .asIcon(),
                new StrictAction() {
                    @Override
                    public void onLeftClick(@Nonnull Player player) {
                        final PlayerInventory inventory = player.getInventory();

                        inventory.addItem(data.createFinalItem());
                        player.closeInventory();

                        Message.success(player, "There is your banner!");

                        // Serialize
                        final String serialize = BannerSerializer.serialize(data);

                        Message.clickHover(
                                player,
                                LazyEvent.suggestCommand(serialize),
                                LazyEvent.showText("&eClick to copy!"),
                                "Use &e/banner &e&n%s&7 to share your banner! &6&l&nCOPY".formatted(serialize)
                        );
                    }

                    @Override
                    public void onRightClick(@Nonnull Player player) {
                        new BannerEditorBaseColorGUI(player, data);
                    }
                }
        );

        // Load patterns
        final int startIndex = page * ITEMS_PER_PAGE - ITEMS_PER_PAGE;

        for (int i = 0; i < SLOTS.length; i++) {
            final int index = startIndex + i;
            final int layer = index + 1;
            final int slot = SLOTS[i];

            // Max patterns
            if (index >= BannerEditor.MAX_PATTERNS) {
                setItem(slot, makeMaxLayersItem(layer));
                continue;
            }

            // Check for moving
            if (moveIndex != MAGIC_MOVE_INDEX) {
                if (moveIndex == index || index >= data.size()) {
                    boolean isSameIndex = moveIndex == index;

                    setItem(
                            slot,
                            new ItemBuilder(isSameIndex ? Material.YELLOW_DYE : Material.RED_DYE)
                                    .setName("&cLayer " + layer)
                                    .addLore()
                                    .addLore("&cCannot move here!")
                                    .addLore("&8&o" + (isSameIndex ? "It's the same place!" : "There is no pattern here!"))
                                    .addLore()
                                    .addLore("&8◦ &eLeft-Click to cancel")
                                    .predicate(isSameIndex, ItemBuilder::glow)
                                    .asIcon(),
                            player -> {
                                moveIndex = MAGIC_MOVE_INDEX;
                                openInventory();
                            }
                    );
                }
                else {
                    setItem(
                            slot,
                            new ItemBuilder(Material.LIME_DYE)
                                    .setName("Layer " + layer)
                                    .addLore()
                                    .addLore("&8◦ &eLeft-Click to move here")
                                    .asIcon(),
                            player -> {
                                data.move(moveIndex, index);
                                moveIndex = MAGIC_MOVE_INDEX;
                                openInventory();
                            }
                    );
                }
            }
            else {
                // No pattern
                if (index >= data.size()) {
                    setItem(
                            slot, new ItemBuilder(Material.GRAY_DYE)
                                    .setAmount(layer)
                                    .setName("Layer " + layer)
                                    .addTextBlockLore("""
                                            &8No pattern yet!
                                            
                                            &7&o;;Click the button below to add a pattern!
                                            """)
                                    .asIcon()
                    );
                    continue;
                }

                final ItemBuilder builder = data.createItem(startIndex, index + 1, true);
                builder.setName("Layer " + layer);

                builder.addLore();
                builder.addLore("&8◦ &eLeft-Click to modify");
                builder.addLore("&8◦ &6Right-Click to remove");
                builder.addLore("&8◦ &dMiddle-Click to move");

                setItem(
                        slot, builder.asIcon(), new StrictAction() {
                            @Override
                            public void onLeftClick(@Nonnull Player player) {
                                new BannerEditorLayerGUI(player, index, BannerEditorGUI.this);
                            }

                            @Override
                            public void onRightClick(@Nonnull Player player) {
                                data.removePattern(index);
                                openInventory();
                            }

                            @Override
                            public void onMiddleClick(@Nonnull Player player) {
                                moveIndex = index;
                                openInventory();
                            }
                        }
                );
            }
        }

        // Page arrows
        if (page > 1) {
            setItem(
                    27,
                    PlayerPageGUI.ICON_PAGE_PREVIOUS,
                    player -> {
                        page--;
                        openInventory();
                    }
            );
        }

        if (page * ITEMS_PER_PAGE <= BannerEditor.MAX_PATTERNS) {
            setItem(
                    35,
                    PlayerPageGUI.ICON_PAGE_NEXT,
                    player -> {
                        page++;
                        openInventory();
                    }
            );
        }

        // Add pattern button
        if (moveIndex == MAGIC_MOVE_INDEX) {
            if (data.size() == BannerEditor.MAX_PATTERNS) {
                setItem(40, makeMaxLayersItem(1));
            }
            else {
                setItem(
                        40,
                        new ItemBuilder(Material.LIME_DYE)
                                .setName("&aAdd Layer")
                                .addTextBlockLore("""
                                        Adds a new layer to the banner.
                                        
                                        &8◦ &eLeft-Click to add a layer
                                        """)
                                .asIcon(),
                        player -> new BannerEditorLayerGUI(player, this)
                );
            }
        }

        // Close button
        setPanelCloseMenu(PanelSlot.CENTER);

        // Clear button
        setPanelItem(
                6, new ItemBuilder(Material.WATER_BUCKET)
                        .setName("Reset")
                        .addTextBlockLore("""
                                Resets the banner editor to the default state.
                                
                                &8◦ &eLeft-Click to reset
                                """)
                        .asIcon(), player -> {
                    Main.getRegistry().bannerEditor.remove(player);

                    PlayerLib.playSound(player, Sound.ENTITY_VILLAGER_WORK_LEATHERWORKER, 2.0f);

                    new BannerEditorGUI(player);
                }
        );
    }


    private ItemStack makeMaxLayersItem(int layer) {
        return new ItemBuilder(Material.RED_DYE)
                .setAmount(layer)
                .setName("&cMax Layers Reached!")
                .addLore()
                .addLore("You cannot add any more layers!")
                .asIcon();
    }
}
