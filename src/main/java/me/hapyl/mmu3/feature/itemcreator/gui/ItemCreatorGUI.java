package me.hapyl.mmu3.feature.itemcreator.gui;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.itemcreator.Category;
import me.hapyl.mmu3.feature.itemcreator.ItemCreator;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.utils.PanelGUI;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.inventory.SignGUI;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ItemCreatorGUI extends PanelGUI {

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

        setClick(
                13,
                player -> new AmountSubGUI(player, "Amount", "Choose new amount for the item. (1-64)", creator().getMaterial(), 64) {
                    @Override
                    public void onClose(int amount) {
                        creator().setAmount(amount);
                    }

                    @Override
                    public PanelGUI returnToGUI() {
                        return new ItemCreatorGUI(getPlayer());
                    }
                },
                ClickType.RIGHT
        );

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
                new ItemBuilder(Material.LIME_DYE)
                        .setName("Build Item")
                        .addSmartLore("Finalize the item and get it. You can still modify the item later!")
                        .build(),
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

        setPanelItem(
                6,
                new ItemBuilder(Material.COMPARATOR)
                        .setName("Complex Build")
                        .setSmartLore("Generate a minecraft command or ItemBuilder code.")
                        .build(),
                this::notImplemented
        );

        // Import/Export
        //        setPanelItem(1, new ItemBuilder(Material.MAP).setName("&aExport").build(), player -> Chat.sendClickableMessage(
        //                player,
        //                LazyClickEvent.COPY_TO_CLIPBOARD.of(creator().exportCode()),
        //                Message.PREFIX + "&e&lCLICK HERE &7to copy export code!"
        //        ));
        setPanelItem(1, new ItemBuilder(Material.MAP).setName("&aExport").build(), this::notImplemented);
        setPanelItem(2, new ItemBuilder(Material.WRITABLE_BOOK).setName("&aImport").build(), this::notImplemented);

        // Lore
        setItem(
                30,
                new ItemBuilder(Material.BOOK).setName("Lore").addSmartLore("Add or remove lore from the item.").build(),
                LoreSubGUI::new
        );

        // Enchants
        setItem(
                32,
                new ItemBuilder(Material.LAPIS_LAZULI)
                        .setName("Enchantments")
                        .addSmartLore("Manager enchantments of the item.").build(),
                EnchantSubGUI::new
        );

        setItem(34, new ItemBuilder(Material.STRUCTURE_VOID).setName("Attributes").build(), this::notImplemented);
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
