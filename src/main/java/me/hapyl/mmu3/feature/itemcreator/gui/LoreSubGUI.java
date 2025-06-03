package me.hapyl.mmu3.feature.itemcreator.gui;

import me.hapyl.eterna.module.inventory.gui.PlayerPageGUI;
import me.hapyl.mmu3.feature.itemcreator.ItemCreator;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.util.StaticItemIcon;
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

    public static final int MAX_LORE_LINES = 21;

    private int start;

    public LoreSubGUI(Player player) {
        super(player, "Modify Lore", Size.FIVE);

        openInventory();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

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
                                .setName("Line " + (index + 1))
                                .setAmount(index + 1)
                                .addLore()
                                .addLore("&aCurrent Lore:")
                                .addLore(currentLore)
                                .addLore()
                                .addLore("&8◦ &eLeft-Click to change line")
                                .addLore("&8◦ &6Right-Click to remove line")
                                .asIcon()
                );
                setAction(
                        slot, player -> new SignGUI(player, "Enter Lore") {
                            @Override
                            public void onResponse(Response response) {
                                lore.set(index, response.getAsString());
                                response.runSync(() -> openInventory(start));
                            }
                        }, ClickType.LEFT
                );
                setAction(
                        slot, player -> {
                            lore.remove(index);
                            openInventory(start);
                        }, ClickType.RIGHT
                );
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
                                .addTextBlockLore("""
                                        &8There is no lore yet!
                                        
                                        Click a flower to insert a new line.
                                        """)
                                .build()
                );
            }
            ++slot;
        }

        // Preview Lore
        setItem(13, buildPreviewItem());

        // Page Arrows
        if (start >= 7) {
            setItem(18, PlayerPageGUI.ICON_PAGE_PREVIOUS, player -> openInventory(start - 7));
        }

        if (start < MAX_LORE_LINES) {
            setItem(26, PlayerPageGUI.ICON_PAGE_NEXT, player -> openInventory(start + 7));
        }

        // Destroy Lore
        setItem(
                29, StaticItemIcon.LoreEditor.CLEAR, player -> {
                    creator.getLore().clear();
                    Message.sound(player, Sound.ENTITY_CAT_HISS, 2.0f);
                    openInventory(0);
                }
        );

        // Add line
        if (lore.size() >= MAX_LORE_LINES) {
            setItem(
                    31,
                    new ItemBuilder(Material.RED_DYE)
                            .setName("&4Cannot Fit More!")
                            .addTextBlockLore("""
                                    You cannot add more lines because you've reached the limit!
                                    """)
                            .asIcon()
            );
        }
        else {
            setItem(
                    31,
                    new ItemBuilder(Material.POPPY)
                            .setName("Add Line")
                            .addTextBlockLore("""
                                    Adds a new line of lore to the item.
                                    
                                    &8◦ &eLeft-Click to add lore
                                    """)
                            .build(),
                    player -> new SignGUI(player, "", "", "^^ Enter Lore ^^") {
                        @Override
                        public void onResponse(Response response) {
                            creator.getLore().add(response.getAsString());
                            runSync(() -> {
                                openInventory(start);
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
                    new ItemBuilder(Material.RED_DYE)
                            .setName("&4Cannot Fit More!")
                            .addTextBlockLore("""
                                    You cannot add more lines because you've reached the limit!
                                    """)
                            .asIcon()
            );
        }
        else {
            setItem(
                    33, StaticItemIcon.LoreEditor.EDIT, player -> {
                        player.closeInventory();
                        Chat.sendClickableHoverableMessage(
                                player,
                                LazyClickEvent.SUGGEST_COMMAND.of("/ic addsmartlore "),
                                LazyHoverEvent.SHOW_TEXT.of("&7Click to add smart lore!"),
                                Message.PREFIX + "&6&lCLICK HERE &7to add smart lore."
                        );
                    }
            );
        }
    }

    public void openInventory(int start) {
        this.start = start;

        openInventory();
    }


    private ItemStack buildPreviewItem() {
        final ItemBuilder builder = new ItemBuilder(Material.WRITTEN_BOOK).setName("Lore Preview").hideFlags();
        for (String string : creator().getLore()) {
            builder.addLore(string);
        }
        return builder.toItemStack();
    }

}
