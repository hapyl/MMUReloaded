package me.hapyl.mmu3.feature.itemcreator.gui;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.itemcreator.ItemCreator;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.utils.PanelGUI;
import org.bukkit.entity.Player;

public abstract class ItemCreatorSubGUI extends PanelGUI {

    private final PanelGUI returnGUI;

    public ItemCreatorSubGUI(Player player, String name, Size size) {
        this(player, name, size, null);
    }

    public ItemCreatorSubGUI(Player player, String name, Size size, PanelGUI returnGUI) {
        super(player, "Item Creator %s %s".formatted(Message.ARROW, name), size);

        this.returnGUI = returnGUI == null ? new ItemCreatorGUI(player) : returnGUI;
        this.clearSubGUI();
    }

    public final void updateAndOpen() {
        updateInventory();
        openInventory();
    }

    public PanelGUI getPanelBack() {
        return new ItemCreatorGUI(getPlayer());
    }

    public void clearSubGUI() {
        clearEverything();
        fillPanel();

        setPanelGoBack(PanelSlot.CENTER, returnGUI.getName(), (player) -> {
            returnGUI.updateInventory();
            returnGUI.openInventory();
        });
    }

    public final ItemCreator creator() {
        return Main.getItemCreator().getCreator(getPlayer());
    }

}
