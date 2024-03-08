package me.hapyl.mmu3.feature.itemcreator.gui;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.itemcreator.Category;
import me.hapyl.mmu3.feature.itemcreator.ItemCreator;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.utils.PanelGUI;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.chat.LazyClickEvent;
import me.hapyl.spigotutils.module.chat.LazyEvent;
import me.hapyl.spigotutils.module.chat.LazyHoverEvent;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.inventory.Response;
import me.hapyl.spigotutils.module.inventory.SignGUI;
import me.hapyl.spigotutils.module.util.Runnables;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public final class ItemCreatorGUI extends PanelGUI {

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
        setItem(13, creator().buildPreviewItem());
        setClick(13, player -> new MaterialSubGUI(player, Category.BLOCKS), ClickType.LEFT);

        final ItemCreator creator = creator();
        setClick(
                13,
                player -> new AmountSubGUI(player, "Amount", "Choose new amount for the item. (1-64)", creator.getMaterial(), 64, null) {
                    @Override
                    public void onClose(int amount) {
                        creator().setAmount(amount);
                    }

                    @Override
                    public PanelGUI returnToGUI() {
                        return new ItemCreatorGUI(player);
                    }
                },
                ClickType.RIGHT
        );

        // Custom Name
        setItem(
                28,
                new ItemBuilder(Material.OAK_SIGN).setName("&aSet Name")
                        .setSmartLore("Add a custom name to your item to make it look cool. Color codes are supported! (&)")
                        .addLore()
                        .addLore("Current Name: " + (creator.hasName() ? creator.getName() : "None"))
                        .addLore()
                        .addLore("&eLeft Click to change name")
                        .addLore("&6Right Click to reset name")
                        .build()
        );

        setClick(28, player -> new SignGUI(player, "", "", "^^ Enter Name ^^") {
            @Override
            public void onResponse(Response response) {
                creator.setName(response.getAsString());
                updateAndReopenSync();
            }

        }, player -> {
            creator.setName(null);
            updateInventory();
            openInventory();
            Message.sound(player, Sound.ENTITY_CAT_AMBIENT, 2.0f);
        });

        // Build Item
        setPanelItem(
                7,
                new ItemBuilder(Material.CHEST).setName("Build Item")
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
                        Message.success(player, "Enjoy your item ;)");
                        Message.sound(player, Sound.BLOCK_ANVIL_USE, 2.0f);
                        player.closeInventory();
                    }
                }
        );

        setPanelItem(
                6,
                new ItemBuilder(Material.COMPARATOR).setName("Complex Build")
                        .setSmartLore("Generate a minecraft command or ItemBuilder code.")
                        .build(),
                player -> {
                    final String command = creator.buildMinecraftGiveItem();

                    Message.clickHover(
                            player,
                            LazyEvent.copyToClipboard(command),
                            LazyEvent.showText("&eClick to copy! &c&o(Colors will only work in &nmcfunc&c&o files!)"),
                            "&e&lCLICK &7to copy minecraft command."
                    );

                    closeInventory();
                }
        );

        // Lore
        setItem(
                30,
                new ItemBuilder(Material.BOOK).setName("Lore")
                        .addSmartLore("Add or remove lore from the item.")
                        .addLore()
                        .addLore("&eClick to open lore sub-menu.")
                        .build(),
                LoreSubGUI::new
        );

        // Enchants
        setItem(
                32,
                new ItemBuilder(Material.LAPIS_LAZULI).setName("Enchantments")
                        .addSmartLore("Manage enchantments of the item.")
                        .addLore()
                        .addLore("&eClick to open enchantments sub-menu.")
                        .build(),
                EnchantSubGUI::new
        );

        // Custom Model Data
        final int customModelData = creator.getCustomModelData();
        setItem(
                33,
                ItemBuilder.of(Material.PAINTING, "Custom Model Data", "Set custom model data value")
                        .addLore()
                        .addLore("Current Value: %s%s".formatted(customModelData, customModelData == 0 ? " &8Default" : ""))
                        .addLore()
                        .addLore("&eClick to set custom model data")
                        .build(),
                player -> {
                    new SignGUI(player, "Enter Custom Model Data") {
                        @Override
                        public void onResponse(Response response) {
                            creator.setCustomModelData(response.getInt(0));
                            updateAndReopenSync();
                        }
                    };
                }
        );

        setItem(34, new ItemBuilder(Material.PRISMARINE_SHARD).setName("Attributes").build(), AttributeSubGUI::new);

        // Material specific special buttons
        switch (creator.getMaterial()) {
            case PLAYER_HEAD -> {
                final String headTexture = creator.getHeadTexture();
                setItem(
                        14,
                        new ItemBuilder(Material.BIRCH_SIGN).setName("&aSet Head Texture")
                                .addSmartLore("Paste &b&nMinecraft-URL&r &7value from &e&nminecraft-heads.com&r &7to set the head texture.")
                                .addLore()
                                .addLore("&aCurrent texture:")
                                .addLore(headTexture == null ? "None!" : headTexture)
                                .addLore()
                                .addLore("&eClick to set texture")
                                .addLoreIf("&6Right Click to remove", headTexture != null)
                                .build()
                );

                setClick(14, player -> {
                    Chat.sendClickableHoverableMessage(
                            player,
                            LazyClickEvent.SUGGEST_COMMAND.of("/ic setheadtexture "),
                            LazyHoverEvent.SHOW_TEXT.of("&7Click to set head texture!"),
                            Message.PREFIX + "&e&lCLICK HERE &7to set head texture."
                    );
                    player.closeInventory();
                }, player -> {
                    creator.setHeadTexture(null);
                    updateInventory();
                });
            }

            case LEATHER_HELMET, LEATHER_CHESTPLATE, LEATHER_LEGGINGS, LEATHER_BOOTS, LEATHER_HORSE_ARMOR -> {
                final Color color = creator.getArmorColor();
                setItem(
                        14,
                        ItemBuilder.of(Material.WHITE_DYE, "Change Color")
                                .addSmartLore("Change leather armor color.")
                                .addLore()
                                .addLore("&aCurrent Color: %s".formatted(
                                        color == null ? "&8None!" : (creator.getArmorColorAsChatColor() + ""))
                                )
                                .addLore()
                                .addLore("&eClick to change color")
                                .asIcon(), ArmorColorSubGUI::new
                );
            }
        }

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

            new ItemCreatorSubGUI(player, "Set Item", Size.THREE) {
                @Override
                public void updateInventory() {
                    setItem(13, currentItem);
                    setItem(
                            11,
                            new ItemBuilder(Material.LIME_TERRACOTTA).setName("&aReplace Item")
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
            }.updateAndOpen();

        });
    }

    private void notImplemented(Player player) {
        Message.error(getPlayer(), "This feature is not yet implemented!");
        Message.sound(getPlayer(), Sound.ENTITY_VILLAGER_NO);
    }

    private void updateAndReopenSync() {
        Runnables.runSync(() -> {
            updateInventory();
            openInventory();
        });
    }

    private ItemStack buildFinalItem() {
        return creator().buildFinalItem();
    }

    private ItemCreator creator() {
        return Main.getItemCreator().getCreator(getPlayer());
    }

}
