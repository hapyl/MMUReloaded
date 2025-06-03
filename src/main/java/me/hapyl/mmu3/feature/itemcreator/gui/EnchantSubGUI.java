package me.hapyl.mmu3.feature.itemcreator.gui;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.inventory.Enchant;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.inventory.gui.PlayerPageGUI;
import me.hapyl.mmu3.feature.itemcreator.ItemCreator;
import me.hapyl.mmu3.message.Message;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class EnchantSubGUI extends ItemCreatorSubGUI {

    private int start;

    public EnchantSubGUI(Player player) {
        super(player, "Enchantments", Size.FIVE);

        openInventory();
    }

    public void openInventory(int start) {
        this.start = start;
        openInventory();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        final ItemCreator creator = creator();
        final Map<Enchantment, Integer> presentEnchants = creator.getEnchantMap();
        final Enchant[] enchants = Enchant.values();

        int slot = 19;
        for (int i = 0; i < 14; i++) {
            int index = start + i;
            if (enchants.length > index) {
                final Enchant enchant = enchants[index];
                final Enchantment bukkitEnchant = enchant.getAsBukkit();
                final boolean isPresent = presentEnchants.containsKey(bukkitEnchant);
                final String enchantName = Chat.capitalize(enchant);

                setItem(
                        slot,
                        new ItemBuilder(enchant.getMaterial())
                                .setName((isPresent ? "&2" : "&c") + enchantName)
                                .addSmartLore(enchant.getDescription())
                                .addLore()
                                .addLore("Max Vanilla Level: &b%s".formatted(enchant.getVanillaMaxLvl()))
                                .addLore()
                                .addLore("&8◦ &eLeft-Click to enchant")
                                .addLoreIf("&8◦ &6Right-Click to disenchant", isPresent)
                                .predicate(isPresent, ItemBuilder::glow)
                                .hideFlags()
                                .build()
                );

                setAction(slot, player -> new AmountSubGUI(
                        player,
                        "Enchant Level",
                        """
                                For %s.
                                """.formatted(enchantName),
                        enchant.getMaterial(),
                        1, 1000,
                        this
                ) {
                    @Override
                    public void onClose(int amount) {
                        presentEnchants.put(bukkitEnchant, amount);
                        Message.sound(player, Sound.ENTITY_PLAYER_LEVELUP, 2.0f);

                        EnchantSubGUI.this.openInventory(start);
                    }
                }, ClickType.LEFT);

                if (isPresent) {
                    setAction(slot, player -> {
                        presentEnchants.remove(bukkitEnchant);
                        Message.info(player, "Removed %s from enchants.".formatted(enchantName));

                        openInventory(start);
                    }, ClickType.RIGHT);
                }

            }

            slot += slot % 9 == 7 ? 3 : 1;
        }

        // Preview Lore
        setItem(13, buildPreviewItem(), ItemCreatorGUI::new);

        // Page Arrows
        if (start >= 14) {
            setItem(39, PlayerPageGUI.ICON_PAGE_PREVIOUS, player -> openInventory(start - 14));
        }

        if (start < (enchants.length - start)) {
            setItem(41, PlayerPageGUI.ICON_PAGE_NEXT, player -> openInventory(start + 14));
        }
    }

    public ItemStack buildPreviewItem() {
        final ItemBuilder builder = new ItemBuilder(Material.ENCHANTED_BOOK).setName("&2Enchantments Preview");

        if (creator().getEnchantMap().isEmpty()) {
            builder.addLore("&8None!");
        }
        else {
            creator().getEnchantMap().forEach((enchant, lvl) -> {
                builder.addLore(" &9%s %s".formatted(Chat.capitalize(enchant.getKey().getKey()), lvl));
            });
        }
        return builder.addLore().addLore("&8◦ &eLeft-Click to open confirm").build();
    }

}
