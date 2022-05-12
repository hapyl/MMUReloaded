package me.hapyl.mmu3.feature.itemcreator.gui;

import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.chat.LazyClickEvent;
import kz.hapyl.spigotutils.module.inventory.ItemBuilder;
import kz.hapyl.spigotutils.module.inventory.SignGUI;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.Message;
import me.hapyl.mmu3.feature.itemcreator.Category;
import me.hapyl.mmu3.feature.itemcreator.ItemCreator;
import me.hapyl.mmu3.utils.PanelGUI;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ItemCreatorGUI extends PanelGUI {

    private final ItemStack ITEM_INCREASE_AMOUNT = ItemBuilder
            .playerHead(
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2VkZDIwYmU5MzUyMDk0OWU2Y2U3ODlkYzRmNDNlZmFlYjI4YzcxN2VlNmJmY2JiZTAyNzgwMTQyZjcxNiJ9fX0=")
            .setName("&aIncrease Amount")
            .addLore("&eLeft Click to increase by 1.")
            .addLore("&eRight Click to increase by 10.")
            .toItemStack();

    private final ItemStack ITEM_DECREASE_AMOUNT = ItemBuilder
            .playerHead(
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ4YTk5ZGIyYzM3ZWM3MWQ3MTk5Y2Q1MjYzOTk4MWE3NTEzY2U5Y2NhOTYyNmEzOTM2Zjk2NWIxMzExOTMifX19")
            .setName("&aDecrease Amount")
            .addLore("&eLeft Click to decrease by 1.")
            .addLore("&eRight Click to decrease by 10.")
            .toItemStack();

    public ItemCreatorGUI(Player player) {
        this(player, false, true);
    }

    public ItemCreatorGUI(Player player, boolean force) {
        this(player, force, false);
    }

    public ItemCreatorGUI(Player player, boolean force, boolean silent) {
        super(player, "Item Creator", Size.FIVE);

        if (creator() == null || force) {
            final ItemCreator creator = new ItemCreator(player);
            Main.getItemCreator().setCreator(player, creator);
            if (!silent) {
                Message.info(player, "Creating new instance of item creator...");
            }
        }
        else if (!silent) {
            Message.info(player, "Opening existing instance...");
        }

        updateInventory();
        openInventory();
    }

    @Override
    public void updateInventory() {
        setItem(13, buildPreviewItem());
        setClick(13, player -> new MaterialSubGUI(player, Category.BLOCKS), ClickType.LEFT);
        setClick(13, player -> new ICSubGUI(player, "Set Amount", Size.THREE) {
            @Override
            public void updateInventory() {
                final ItemCreator creator = creator();

                this.setItem(
                        13,
                        new ItemBuilder(creator.getMaterial())
                                .setName("&a" + creator.getName())
                                .setAmount(creator.getAmount())
                                .addSmartLore("Item may have any amount between 1 and 64.")
                                .addLore()
                                .addLore("&eClick to confirm")
                                .build(),
                        ItemCreatorGUI::new
                );

                setItemChangeAmount(15, ITEM_INCREASE_AMOUNT, true);
                setItemChangeAmount(11, ITEM_DECREASE_AMOUNT, false);
            }

            private void setItemChangeAmount(int slot, ItemStack item, boolean increase) {
                setItem(slot, item);
                setClick(slot, p -> {
                    creator().setAmount(creator().getAmount() + (increase ? 1 : -1));
                    Message.sound(getPlayer(), Sound.UI_BUTTON_CLICK, 1.75f);
                    updateInventory();
                }, ClickType.LEFT);
                setClick(slot, p -> {
                    creator().setAmount(creator().getAmount() + (increase ? 10 : -10));
                    Message.sound(getPlayer(), Sound.UI_BUTTON_CLICK, 2.0f);
                    updateInventory();
                }, ClickType.RIGHT);
            }
        }, ClickType.RIGHT);

        // Custom Name
        setItem(
                28,
                new ItemBuilder(Material.OAK_SIGN)
                        .setName("&aSet Name")
                        .setSmartLore("Add a custom name to your item to make it look cool. Color codes are supported! (&)")
                        .addLore()
                        .addLore("Current Name: " + (creator().hasName() ? creator().getName() : "None"))
                        .addLore()
                        .addLore("&eLeft Click to change name")
                        .addLore("&eRight Click to reset name")
                        .build()
        );

        setClick(28, player -> new SignGUI(player, "Enter Name") {
            @Override
            public void onResponse(Player player, String[] strings) {
                creator().setName(concatString(strings));
                runSync(() -> {
                    updateInventory();
                    openInventory();
                });
            }
        }.openMenu(), ClickType.LEFT);

        setClick(28, player -> {
            creator().setName(null);
            updateInventory();
            openInventory();
            Message.sound(player, Sound.ENTITY_CAT_AMBIENT, 2.0f);
        }, ClickType.RIGHT);

        // Build Item
        setPanelItem(
                7,
                new ItemBuilder(Material.LIME_DYE).setName("&aBuild Item").addLore("Finalize your item and get it.").build(),
                player -> {
                    final PlayerInventory inventory = player.getInventory();
                    if (inventory.firstEmpty() == -1) {
                        Message.error(player, "Please empty your inventory first!");
                        Message.sound(player, Sound.ENTITY_VILLAGER_NO);
                    }
                    else {
                        inventory.addItem(buildFinalItem());
                        Message.success(player, "Your item was successfully built!");
                        Message.sound(player, Sound.BLOCK_ANVIL_USE, 2.0f);
                        player.closeInventory();
                    }
                }
        );

        // Import/Export
        setPanelItem(1, new ItemBuilder(Material.MAP).setName("&aExport").build(), player -> Chat.sendClickableMessage(
                player,
                LazyClickEvent.COPY_TO_CLIPBOARD.of(creator().exportCode()),
                Message.PREFIX + "&e&lCLICK HERE &7to copy export code!"
        ));
        setPanelItem(2, new ItemBuilder(Material.WRITABLE_BOOK).setName("&aImport").build(), this::notImplemented);

        // Lore
        setItem(
                30,
                new ItemBuilder(Material.BOOK).setName("&aModify Lore").addSmartLore("Add or remove lore from your item.").build(),
                LoreSubGUI::new
        );

        // TODO: 009. 09/05/2022 - Finish enchant, attributes etc

        // Add listener for creative item replace
        setEventListener((player, self, ev) -> {
            final int slot = ev.getRawSlot();
            final ItemStack currentItem = ev.getCurrentItem();

            if (slot < getSize() || player.getGameMode() != GameMode.CREATIVE || currentItem == null) {
                return;
            }

            final Material type = currentItem.getType();
            if (type.isAir() || !type.isItem()) {
                return;
            }

            new ICSubGUI(player, "Set Item", Size.THREE) {
                @Override
                public void updateInventory() {
                    setItem(13, currentItem);
                    setItem(
                            11,
                            new ItemBuilder(Material.LIME_TERRACOTTA)
                                    .setName("&aReplace Item")
                                    .setSmartLore("Replace editor item with item to the right.")
                                    .addLore()
                                    .addSmartLore("&c&lWarning! &7This will reset current editor item, including any changes you made.")
                                    .build(),
                            player -> {
                                creator().setItem(currentItem);
                                ItemCreatorGUI.this.updateInventory();
                                ItemCreatorGUI.this.openInventory();
                            }
                    );
                    setItem(
                            15,
                            new ItemBuilder(Material.RED_TERRACOTTA).setName("&aCancel").setSmartLore("Return to the editor.").build(),
                            player -> ItemCreatorGUI.this.openInventory()
                    );
                }
            };

        });

    }

    private void notImplemented(Player player) {
        Message.error(getPlayer(), "This feature is not yet implemented!");
        Message.sound(getPlayer(), Sound.ENTITY_VILLAGER_NO);
    }

    private ItemStack buildPreviewItem() {
        final ItemBuilder builder = new ItemBuilder(buildFinalItem());

        builder.addLore("&8&m-------------------------");
        builder.addSmartLore("This is a preview of your item! Click the button at the bottom to claim your item.");
        builder.addLore();
        builder.addLore("&eLeft Click to change material.");
        builder.addLore("&eRight Click to pick amount.");

        return builder.toItemStack();
    }

    private ItemStack buildFinalItem() {
        return creator().buildFinalItem();
    }

    private ItemCreator creator() {
        return Main.getItemCreator().getCreator(getPlayer());
    }

}
