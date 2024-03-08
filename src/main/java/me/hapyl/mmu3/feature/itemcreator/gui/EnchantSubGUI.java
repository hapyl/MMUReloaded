package me.hapyl.mmu3.feature.itemcreator.gui;

import me.hapyl.mmu3.feature.itemcreator.ItemCreator;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.utils.PanelGUI;
import me.hapyl.mmu3.utils.StaticItemIcon;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.inventory.Enchant;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class EnchantSubGUI extends ItemCreatorSubGUI {

    public EnchantSubGUI(Player player) {
        super(player, "Enchantments", Size.FIVE);
        updateInventory(0);
        openInventory();
    }

    public void updateInventory(int start) {
        clearSubGUI();

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
                                .setName((isPresent ? "&a&l" : "&c") + enchantName)
                                .addSmartLore(enchant.getDescription())
                                .addLore()
                                .addLore("Max vanilla level: &b%s".formatted(enchant.getVanillaMaxLvl()))
                                .addLore()
                                .addLore("&eClick to choose level")
                                .addLoreIf("&eRight Click to remove enchant", isPresent)
                                .predicate(isPresent, ItemBuilder::glow)
                                .hideFlags()
                                .build()
                );

                setClick(slot, player -> new AmountSubGUI(
                        player,
                        "Enchant Level",
                        "For " + enchantName,
                        enchant.getMaterial(), 1000, new EnchantSubGUI(player)
                ) {
                    @Override
                    public void onClose(int amount) {
                        presentEnchants.put(bukkitEnchant, amount);
                        EnchantSubGUI.this.updateInventory(start);
                        Message.sound(getPlayer(), Sound.ENTITY_PLAYER_LEVELUP, 2.0f);
                    }

                    @Override
                    public PanelGUI returnToGUI() {
                        return new EnchantSubGUI(getPlayer());
                    }
                }, ClickType.LEFT);

                if (isPresent) {
                    setClick(slot, player -> {
                        presentEnchants.remove(bukkitEnchant);
                        Message.info(player, "Removed %s from enchants.", enchantName);
                        updateInventory(start);
                    }, ClickType.RIGHT);
                }

            }

            slot += slot % 9 == 7 ? 3 : 1;
        }

        // Preview Lore
        setItem(13, buildPreviewItem(), ItemCreatorGUI::new);

        // Page Arrows
        if (start >= 14) {
            setItem(39, StaticItemIcon.PAGE_PREVIOUS, player -> updateInventory(start - 14));
        }

        if (start < (enchants.length - start)) {
            setItem(41, StaticItemIcon.PAGE_NEXT, player -> updateInventory(start + 14));
        }

    }

    @Override
    public void updateInventory() {
        updateInventory(0);
    }

    public ItemStack buildPreviewItem() {
        final ItemBuilder builder = new ItemBuilder(Material.LAPIS_LAZULI).setName("Enchantments Preview:");
        if (creator().getEnchantMap().isEmpty()) {
            builder.addLore("&8None!");
        }
        else {
            creator().getEnchantMap().forEach((enchant, lvl) -> {
                builder.addLore(" &9%s &l%s".formatted(Chat.capitalize(enchant.getKey().getKey()), lvl));
            });
        }
        return builder.addLore().addLore("&eClick to confirm").build();
    }

}
