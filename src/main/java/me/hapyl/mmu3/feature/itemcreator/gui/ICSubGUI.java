package me.hapyl.mmu3.feature.itemcreator.gui;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.itemcreator.ItemCreator;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.utils.PanelGUI;
import org.bukkit.entity.Player;

public abstract class ICSubGUI extends PanelGUI {

    public ICSubGUI(Player player, String name, Size size) {
        this(player, name, size, true);
    }

    public ICSubGUI(Player player, String name, Size size, boolean updateAndOpen) {
        super(player, "Item Creator %s %s".formatted(Message.ARROW, name), size);

        this.clear();

        if (updateAndOpen) {
            updateInventory();
            openInventory();
        }
    }

    public void clear() {
        clearEverything();
        fillPanel();
        setPanelGoBack(PanelSlot.CENTER, "Item Creator", ItemCreatorGUI::new);
    }

    public final ItemCreator creator() {
        return Main.getItemCreator().getCreator(getPlayer());
    }

}
