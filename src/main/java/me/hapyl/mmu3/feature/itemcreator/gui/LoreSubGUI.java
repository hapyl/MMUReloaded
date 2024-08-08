package me.hapyl.mmu3.feature.itemcreator.gui;

import me.hapyl.mmu3.feature.itemcreator.ItemCreator;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.utils.StaticItemIcon;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.chat.LazyClickEvent;
import me.hapyl.eterna.module.chat.LazyHoverEvent;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.inventory.Response;
import me.hapyl.eterna.module.inventory.SignGUI;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LoreSubGUI extends ItemCreatorSubGUI {

    private static final int MAX_LORE_LINES = 21;

    public LoreSubGUI(Player player) {
        super(player, "Modify Lore", Size.FIVE);
        updateInventory(0);
        openInventory();
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
                                .setName("&aLine %s".formatted(index + 1))
                                .setAmount(index + 1)
                                .addLore("&aCurrent Lore:")
                                .addLore(currentLore)
                                .addLore()
                                .hideFlags()
                                .addLore("&eLeft Click to change line.")
                                .addLore("&6Right Click to remove line.")
                                .build()
                );
                setClick(slot, player -> new SignGUI(player, "Enter Lore") {
                    @Override
                    public void onResponse(Response response) {
                        lore.set(index, response.getAsString());
                        response.runSync(() -> {
                            updateInventory(start);
                            openInventory();
                        });
                    }

                }, ClickType.LEFT);
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
                        new ItemBuilder(nextLine ? Material.MAP : Material.PAPER)
                                .setAmount(index + 1)
                                .setName("%sLine %s".formatted((nextLine ? "&e" : "&c"), index + 1))
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
            setItem(18, StaticItemIcon.PAGE_PREVIOUS, player -> updateInventory(start - 7));
        }
        else {
            setItem(18, null);
        }

        if (start < MAX_LORE_LINES) {
            setItem(26, StaticItemIcon.PAGE_NEXT, player -> updateInventory(start + 7));
        }
        else {
            setItem(26, StaticItemIcon.LoreEditor.PAGE_LIMIT);
        }

        // Destroy Lore
        setItem(29, StaticItemIcon.LoreEditor.CLEAR, player -> {
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
                            .addSmartLore("Adds a new line of lore to the item.")
                            .addLore()
                            .addLore("&eClick to add lore")
                            .build(),
                    player -> new SignGUI(player, "", "", "^^ Enter Lore ^^") {
                        @Override
                        public void onResponse(Response response) {
                            creator.getLore().add(response.getAsString());
                            runSync(() -> {
                                updateInventory(start);
                                openInventory();
                            });
                        }
                    }
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
            setItem(33, StaticItemIcon.LoreEditor.EDIT, player -> {
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
