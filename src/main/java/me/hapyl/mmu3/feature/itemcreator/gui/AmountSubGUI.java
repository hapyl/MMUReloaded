package me.hapyl.mmu3.feature.itemcreator.gui;

import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.util.PanelGUI;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public abstract class AmountSubGUI extends ItemCreatorSubGUI {

    private static final ItemStack ITEM_INCREASE_AMOUNT = ItemBuilder
            .playerHeadUrl("3edd20be93520949e6ce789dc4f43efaeb28c717ee6bfcbbe02780142f716")
            .setName("Increase")
            .addLore("&8◦ &eLeft-Click to increase by 1")
            .addLore("&8◦ &6Right-Click to increase by 10")
            .toItemStack();

    private static final ItemStack ITEM_DECREASE_AMOUNT = ItemBuilder
            .playerHeadUrl("bd8a99db2c37ec71d7199cd52639981a7513ce9cca9626a3936f965b131193")
            .setName("Decrease")
            .addLore("&8◦ &eLeft-Click to decrease by 1")
            .addLore("&8◦ &6Right-Click to decrease by 10")
            .toItemStack();

    private final String name;
    private final String about;
    private final Material material;
    private final int maxAmount;
    private int amount;

    public AmountSubGUI(Player player, String name, String about, Material material, int amount, int maxAmount) {
        this(player, name, about, material, amount, maxAmount, null);
    }

    public AmountSubGUI(Player player, String name, String about, Material material, int amount, int maxAmount, @Nullable PanelGUI returnGui) {
        super(player, name, Size.FOUR, returnGui);
        this.name = name;
        this.about = about;
        this.material = material;
        this.amount = amount;
        this.maxAmount = maxAmount;

        openInventory();
    }

    public abstract void onClose(int amount);

    @Override
    public void onUpdate() {
        super.onUpdate();

        setItem(
                22,
                new ItemBuilder(material)
                        .setName("" + name)
                        .setMaximumStackSize(99)
                        .setAmount(amount)
                        .addTextBlockLore(about, "&7&o")
                        .addLore()
                        .addLore("Current %s: &b%s".formatted(name, amount))
                        .addLore()
                        .hideFlags()
                        .addLore("&8◦ &eLeft-Click to confirm")
                        .build(),
                (player) -> onClose(amount)
        );

        setItemChangeAmount(24, true);
        setItemChangeAmount(20, false);
    }

    private void setItemChangeAmount(int slot, boolean increase) {
        setItem(slot, increase ? ITEM_INCREASE_AMOUNT : ITEM_DECREASE_AMOUNT);

        setAction(
                slot, p -> {
                    amount = Math.clamp(amount + (increase ? 1 : -1), 1, maxAmount);
                    Message.sound(player, Sound.UI_BUTTON_CLICK, 1.75f);

                    openInventory();
                }, ClickType.LEFT
        );

        setAction(
                slot, p -> {
                    amount = Math.clamp(amount + (increase ? 10 : -10), 1, maxAmount);
                    Message.sound(player, Sound.UI_BUTTON_CLICK, 2.0f);

                    openInventory();
                }, ClickType.RIGHT
        );
    }
}
