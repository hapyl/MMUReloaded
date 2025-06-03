package me.hapyl.mmu3.util;

import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.inventory.gui.PlayerGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.function.Consumer;

/**
 * This implementation will add extra inventory line if possible, to have consistent panel.
 */
public abstract class PanelGUI extends PlayerGUI {

    public final static ItemStack PANEL_ITEM = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("&0").build();

    private final boolean panel;

    public PanelGUI(Player player, String name, Size size) {
        super(player, name, size == Size.NO_PANEL ? size.getNumeric() : size.getNumeric() + 1);
        panel = size != Size.NO_PANEL;
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void onUpdate() {
        fillPanel();
    }

    public void fillPanel() {
        if (!panel) {
            return;
        }
        fillItem(getSize() - 9, getSize() - 1, PANEL_ITEM);
    }

    public final void setPanelGoBack(int slot, String name, Consumer<Player> action) {
        setPanelItem(slot, goBackItemBuilder(name).asIcon(), action);
    }

    public ItemBuilder goBackItemBuilder(String name) {
        return ItemBuilder.playerHeadUrl("354ce8157e71dcd5b6b1674ac5bd55490702027c675e5cdceac55d2fbbd5a")
                .setName("Go Back")
                .addLore("&7To " + name);
    }

    public final void setPanelGoBack(String name, Consumer<Player> action) {
        setPanelGoBack(2, name, action);
    }

    public final void setPanelCloseMenu(int slot) {
        setPanelItem(
                slot, new ItemBuilder(Material.BARRIER)
                        .setName("Close Menu")
                        .addLore()
                        .addLore("&8◦ &eLeft-Click to close the menu")
                        .asIcon(), Player::closeInventory
        );
    }

    public final void setPanelCloseMenu() {
        setPanelCloseMenu(4);
    }

    /**
     * Sets an item to panel if present.
     * Panel is always last 9 slots.
     *
     * @param slot   - Slot in panel, between 0-8.
     * @param item   - Item to set.
     * @param action - Click event if needed.
     * @param type   - Types of a click.
     */
    public final void setPanelItem(int slot, ItemStack item, @Nonnull Consumer<Player> action, @Nullable ClickType... type) {
        if (!panel) {
            return;
        }

        slot = Math.clamp(slot, 0, 8);
        final int relativeSlot = slot + (getSize() - 9);

        if (type == null || type.length == 0) {
            setItem(relativeSlot, item, action);
        }
        else {
            setItem(relativeSlot, item, action, type);
        }
    }

    public final void setPanelItem(int slot, ItemStack item, Consumer<Player> action) {
        setPanelItem(slot, item, action, new ClickType[0]);
    }

    public enum Size {
        ONE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        NO_PANEL(6);

        private final int rows;

        Size(int rows) {
            this.rows = rows;
        }

        public int getNumeric() {
            return rows;
        }
    }

    public static class PanelSlot {
        public static final int FIRST = 0;
        public static final int CENTER = 4;
        public static final int LAST = 8;
    }

}
