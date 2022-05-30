package me.hapyl.mmu3.feature.itemcreator.gui;

import me.hapyl.mmu3.Message;
import me.hapyl.mmu3.feature.itemcreator.ItemCreator;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.chat.LazyClickEvent;
import me.hapyl.spigotutils.module.chat.LazyHoverEvent;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.inventory.SignGUI;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LoreSubGUI extends ICSubGUI {

    private static final ItemStack ITEM_DESTROY_LORE = new ItemBuilder(Material.RED_GLAZED_TERRACOTTA)
            .setName("&cClear Lore")
            .addSmartLore("Clears all all from the item.")
            .addSmartLore("&c&lWarning! &7This action cannot be undone.")
            .build();

    private static final ItemStack ITEM_ADD_SMART_LORE = new ItemBuilder(Material.ROSE_BUSH)
            .setName("&aAdd Smart Lore")
            .addSmartLore("Adds smart lore to the item.")
            .addSmartLore("Smart lore automatically splits lines for you in the best spots. You can also use '_&7_' to put a manual split.")
            .build();

    private static final ItemStack ITEM_PAGE_NEXT = new ItemBuilder(Material.ARROW).setName("&aNext Page").build();
    private static final ItemStack ITEM_PAGE_PREVIOUS = new ItemBuilder(Material.ARROW).setName("&aPrevious Page").build();
    private static final ItemStack ITEM_PAGE_LAST = new ItemBuilder(Material.REDSTONE_BLOCK)
            .setName("&aLast Page!")
            .addSmartLore("Lines are limited because lore will not fit screen of a player if there would have been more than 28 lines!")
            .build();

    private static final int MAX_LORE_LINES = 21;

    public LoreSubGUI(Player player) {
        super(player, "Modify Lore", Size.FIVE);
        setPanelGoBack(PanelSlot.CENTER, "Item Creator", ItemCreatorGUI::new);
    }

    public void updateInventory(int start) {
        final ItemCreator creator = creator();
        final List<String> lore = creator.getLore();

        int slot = 19;
        for (int i = 0; i < 7; i++) {
            int index = start + i;
            if (lore.size() > index) {
                final String currentLore = lore.get(index);
                setItem(
                        slot,
                        new ItemBuilder(Material.FILLED_MAP)
                                .setName("&aLine %s", index + 1)
                                .setAmount(index + 1)
                                .addLore("Current Lore: " + currentLore)
                                .addLore()
                                .hideFlags()
                                .addLore("&eLeft Click to change line.")
                                .addLore("&eRight Click to remove line.")
                                .build()
                );
                setClick(slot, player -> new SignGUI(player, "Enter Lore") {
                    @Override
                    public void onResponse(Player player, String[] strings) {
                        lore.set(index, concatString(strings));
                        runSync(() -> {
                            updateInventory(start);
                            openInventory();
                        });
                    }
                }.openMenu(), ClickType.LEFT);
                setClick(slot, player -> {
                    lore.remove(index);
                    updateInventory(start);
                    openInventory();
                }, ClickType.RIGHT);
            }
            else {
                boolean nextLine = index == lore.size();
                setItem(
                        slot,
                        new ItemBuilder(Material.MAP)
                                .setAmount(index + 1)
                                .setName("%sLine %s", (nextLine ? "&e" : "&c"), index + 1)
                                .hideFlags()
                                .predicate(nextLine, ItemBuilder::glow)
                                .addSmartLore("There is no lore yet. Click the flower to insert lore to next available slot.")
                                .build()
                );
            }
            ++slot;
        }

        // Preview Lore
        setItem(13, buildPreviewItem());

        // Page Arrows
        if (start >= 7) {
            setItem(18, ITEM_PAGE_PREVIOUS, player -> updateInventory(start - 7));
        }
        else {
            setItem(18, null);
        }

        if (start < MAX_LORE_LINES) {
            setItem(26, ITEM_PAGE_NEXT, player -> updateInventory(start + 7));
        }
        else {
            setItem(26, ITEM_PAGE_LAST);
        }

        // Destroy Lore
        setItem(29, ITEM_DESTROY_LORE, player -> {
            creator.getLore().clear();
            Message.sound(player, Sound.ENTITY_CAT_HISS, 2.0f);
            updateInventory(0);
        });

        // Add line
        if (lore.size() >= MAX_LORE_LINES) {
            setItem(
                    31,
                    new ItemBuilder(Material.REDSTONE_BLOCK).setName("&cCannot add lore!").addSmartLore("Lore lines limit reached!").build()
            );
        }
        else {
            setItem(
                    31,
                    new ItemBuilder(Material.POPPY)
                            .setName("&aAdd Line")
                            .addSmartLore("Adds a new line of lore to the item. Next slot is highlighted!")
                            .build(),
                    player -> new SignGUI(player, "Enter Lore") {
                        @Override
                        public void onResponse(Player player, String[] strings) {
                            creator.getLore().add(concatString(strings));
                            runSync(() -> {
                                updateInventory(start);
                                openInventory();
                            });
                        }
                    }.openMenu()
            );
        }

        // Smart Lore
        if (lore.size() >= MAX_LORE_LINES) {
            setItem(
                    33,
                    new ItemBuilder(Material.REDSTONE_BLOCK).setName("&cCannot add lore!").addSmartLore("Lore lines limit reached!").build()
            );
        }
        else {
            setItem(33, ITEM_ADD_SMART_LORE, player -> {
                player.closeInventory();
                Chat.sendClickableHoverableMessage(
                        player,
                        LazyClickEvent.SUGGEST_COMMAND.of("/ic addsmartlore "),
                        LazyHoverEvent.SHOW_TEXT.of("&7Click to add smart lore!"),
                        Message.PREFIX + "&e&lCLICK HERE &7to add smart lore."
                );
            });
        }

    }

    private ItemStack buildPreviewItem() {
        final ItemBuilder builder = new ItemBuilder(Material.WRITTEN_BOOK).setName("&aLore Preview").hideFlags();
        for (String string : creator().getLore()) {
            builder.addLore(string);
        }
        return builder.toItemStack();
    }

    @Override
    public void updateInventory() {
        updateInventory(0);
    }
}
