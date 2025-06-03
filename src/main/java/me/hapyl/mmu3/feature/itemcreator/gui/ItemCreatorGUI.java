package me.hapyl.mmu3.feature.itemcreator.gui;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.chat.LazyClickEvent;
import me.hapyl.eterna.module.chat.LazyHoverEvent;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.inventory.Response;
import me.hapyl.eterna.module.inventory.SignGUI;
import me.hapyl.eterna.module.inventory.gui.GUIEventListener;
import me.hapyl.eterna.module.player.PlayerLib;
import me.hapyl.eterna.module.util.BukkitUtils;
import me.hapyl.eterna.module.util.Runnables;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.itemcreator.ItemCreator;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.util.PanelGUI;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import javax.annotation.Nonnull;

public final class ItemCreatorGUI extends PanelGUI implements GUIEventListener {

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

        openInventory();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        final ItemCreator creator = creator();

        fillRow(0, PANEL_ITEM);

        setItem(13, creator.buildPreviewItem());
        setAction(13, MaterialSubGUI::new, ClickType.LEFT);

        setAction(
                13,
                player -> new AmountSubGUI(
                        player,
                        "Amount",
                        "Choose new amount for the item. (1-100)",
                        creator.getMaterial(),
                        creator.getAmount(),
                        100
                ) {
                    @Override
                    public void onClose(int amount) {
                        creator().setAmount(amount);
                    }
                },
                ClickType.RIGHT
        );

        // Custom Name
        setItem(
                28,
                new ItemBuilder(Material.OAK_SIGN).setName("Set Name")
                        .addTextBlockLore("""
                                Adds a name to your item to make it look cool and pretty and amazing!
                                &8&o;;Color codes supported! (&)
                                """)
                        .addLore()
                        .addLore("Current Name: &8" + (creator.hasName() ? creator.getName() : "None!"))
                        .addLore()
                        .addLore("&8◦ &eLeft-Click to change name")
                        .addLore("&8◦ &6Right-Click to reset name")
                        .build()
        );

        setAction(
                28, player -> new SignGUI(player, "", "", "^^ Enter Name ^^") {
                    @Override
                    public void onResponse(Response response) {
                        creator.setName(response.getAsString());
                        updateAndReopenSync();
                    }

                }, player -> {
                    creator.setName(null);
                    openInventory();
                    Message.sound(player, Sound.ENTITY_CAT_AMBIENT, 2.0f);
                }
        );

        // Build Item
        setPanelItem(
                PanelSlot.CENTER,
                new ItemBuilder(Material.CHEST)
                        .setName("Build Item")
                        .addTextBlockLore("""
                                Finalizes the item and gives it to you.
                                &8&o;;You can still modify the item after!
                                """)
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

        // Hide Flags
        setPanelItem(
                6, new ItemBuilder(creator.hideFlags ? Material.LIME_DYE : Material.GRAY_DYE)
                        .setName("Hide Flags " + BukkitUtils.checkmark(creator.hideFlags))
                        .addTextBlockLore("""
                                Toggles whether most flags and tooltips are hidden on the item, such as attributes, enchantments, etc.
                                &8&o;;Note that you cannot hide tooltips for Trims and Music Discs!
                                """)
                        .addLore()
                        .addLore("&8◦ &eLeft-Click to %s flags".formatted(creator.hideFlags ? "show" : "hide"))
                        .asIcon(), player -> {
                    creator.hideFlags = !creator.hideFlags;

                    PlayerLib.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f);
                    openInventory();
                }
        );

        // Lore
        setItem(
                30,
                new ItemBuilder(Material.BOOK).setName("Lore")
                        .addTextBlockLore("""
                                Manage lore on the item.
                                
                                &8◦ &eLeft-Click to manage
                                """)
                        .build(),
                LoreSubGUI::new
        );

        // Enchants
        setItem(
                32,
                new ItemBuilder(Material.ENCHANTED_BOOK)
                        .setName("Enchantments")
                        .addTextBlockLore("""
                                Manage enchantments on the item.
                                
                                &8◦ &eLeft-Click to manage
                                """)
                        .build(),
                EnchantSubGUI::new
        );

        setItem(
                34, new ItemBuilder(Material.PRISMARINE_SHARD)
                        .setName("Attributes")
                        .addTextBlockLore("""
                                Manage attribute modifiers on the item.
                                
                                &8◦ &eLeft-Click to manage
                                """)
                        .build(), AttributeSubGUI::new
        );

        // Material specific special buttons
        switch (creator.getMaterial()) {
            case PLAYER_HEAD -> {
                final String headTexture = creator.getHeadTexture();
                setItem(
                        14,
                        new ItemBuilder(Material.BIRCH_SIGN)
                                .setName("Set Head Texture")
                                .addSmartLore("Paste &b&nMinecraft-URL&r &7value from &e&nminecraft-heads.com&r &7to set the head texture.")
                                .addLore()
                                .addLore("Current texture:")
                                .addLore(headTexture == null ? "&8None!" : headTexture)
                                .addLore()
                                .addLore("&8◦ &eLeft-Click to set texture")
                                .addLoreIf("&8◦ &6Right-Right Click to remove", headTexture != null)
                                .build()
                );

                setAction(
                        14, player -> {
                            Chat.sendClickableHoverableMessage(
                                    player,
                                    LazyClickEvent.SUGGEST_COMMAND.of("/ic setheadtexture "),
                                    LazyHoverEvent.SHOW_TEXT.of("&7Click to set head texture!"),
                                    Message.PREFIX + "&6&lCLICK HERE &7to set head texture."
                            );
                            player.closeInventory();
                        }, player -> {
                            creator.setHeadTexture(null);
                            openInventory();
                        }
                );
            }

            case LEATHER_HELMET, LEATHER_CHESTPLATE, LEATHER_LEGGINGS, LEATHER_BOOTS, LEATHER_HORSE_ARMOR -> {
                final Color color = creator.getArmorColor();
                setItem(
                        14,
                        ItemBuilder.of(Material.WHITE_DYE, "Change Color")
                                .addSmartLore("Change leather armor color.")
                                .addLore()
                                .addLore("Current Color: %s".formatted(
                                        color == null ? "&8None!" : (creator.getArmorColorAsChatColor() + "⬛"))
                                )
                                .addLore()
                                .addLore("&8◦ &eLeft-Click to change color")
                                .asIcon(), ArmorColorSubGUI::new
                );
            }
        }

    }

    @Override
    public void onClick(int slot, @Nonnull InventoryClickEvent event) {
        final ItemStack currentItem = event.getCurrentItem();

        if (slot < getSize() || player.getGameMode() != GameMode.CREATIVE || currentItem == null) {
            return;
        }

        final Material type = currentItem.getType();
        if (type.isAir() || !type.isItem()) {
            return;
        }

        new ItemCreatorSubGUI(player, "Set Item", Size.THREE) {

            @Override
            public void onUpdate() {
                super.onUpdate();

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
                            ItemCreatorGUI.this.openInventory();
                        }
                );
                setItem(
                        15,
                        new ItemBuilder(Material.RED_TERRACOTTA).setName("&aCancel").setSmartLore("Return to the editor.").build(),
                        player -> ItemCreatorGUI.this.openInventory()
                );
            }

        }.openInventory();
    }

    private void updateAndReopenSync() {
        Runnables.runSync(this::openInventory);
    }

    private ItemStack buildFinalItem() {
        return creator().buildFinalItem();
    }

    private ItemCreator creator() {
        return Main.getItemCreator().getCreator(player);
    }

}
