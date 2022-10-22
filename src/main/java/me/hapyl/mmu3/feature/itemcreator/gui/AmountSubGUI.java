package me.hapyl.mmu3.feature.itemcreator.gui;

import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.utils.PanelGUI;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.math.Numbers;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public abstract class AmountSubGUI extends ICSubGUI {

    private static final ItemStack ITEM_INCREASE_AMOUNT = ItemBuilder
            .playerHead(
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2VkZDIwYmU5MzUyMDk0OWU2Y2U3ODlkYzRmNDNlZmFlYjI4YzcxN2VlNmJmY2JiZTAyNzgwMTQyZjcxNiJ9fX0=")
            .setName("&aIncrease")
            .addLore("&eLeft Click to increase by 1.")
            .addLore("&6Right Click to increase by 10.")
            .toItemStack();

    private static final ItemStack ITEM_DECREASE_AMOUNT = ItemBuilder
            .playerHead(
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ4YTk5ZGIyYzM3ZWM3MWQ3MTk5Y2Q1MjYzOTk4MWE3NTEzY2U5Y2NhOTYyNmEzOTM2Zjk2NWIxMzExOTMifX19")
            .setName("&aDecrease")
            .addLore("&eLeft Click to decrease by 1.")
            .addLore("&6Right Click to decrease by 10.")
            .toItemStack();

    private final String name;
    private final String about;
    private final Material material;
    private final int maxAmount;
    private int amount;

    public AmountSubGUI(Player player, String name, String about, Material material, int maxAmount) {
        super(player, name, Size.THREE, false);
        this.name = name;
        this.about = about;
        this.material = material;
        this.amount = 1;
        this.maxAmount = maxAmount;
        updateInventory();
    }

    public abstract void onClose(int amount);

    public abstract PanelGUI returnToGUI();

    @Override
    public void updateInventory() {
        setItem(
                13,
                new ItemBuilder(material)
                        .setName(name)
                        .setAmount(amount)
                        .addSmartLore(about)
                        .addLore()
                        .addLore("Current %s: &b%s", name.toLowerCase(Locale.ROOT), amount)
                        .addLore()
                        .hideFlags()
                        .addLore("&eClick to confirm")
                        .build(),
                (player) -> {
                    onClose(amount);
                    final PanelGUI gui = returnToGUI();
                    if (gui != null) {
                        gui.openInventory();
                    }
                }
        );

        setItemChangeAmount(15, true);
        setItemChangeAmount(11, false);

        openInventory();
    }

    private void setItemChangeAmount(int slot, boolean increase) {
        setItem(slot, increase ? ITEM_INCREASE_AMOUNT : ITEM_DECREASE_AMOUNT);
        setClick(slot, p -> {
            amount = Numbers.clamp(amount + (increase ? 1 : -1), 0, maxAmount);
            Message.sound(getPlayer(), Sound.UI_BUTTON_CLICK, 1.75f);
            updateInventory();
        }, ClickType.LEFT);
        setClick(slot, p -> {
            amount = Numbers.clamp(amount + (increase ? 10 : -10), 0, maxAmount);
            Message.sound(getPlayer(), Sound.UI_BUTTON_CLICK, 2.0f);
            updateInventory();
        }, ClickType.RIGHT);
    }
}
