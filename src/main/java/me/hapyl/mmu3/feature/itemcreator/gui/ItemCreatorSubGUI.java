package me.hapyl.mmu3.feature.itemcreator.gui;

import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.itemcreator.ItemCreator;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.util.PanelGUI;
import org.bukkit.entity.Player;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class ItemCreatorSubGUI extends PanelGUI {

    private final PanelGUI returnGUI;

    public ItemCreatorSubGUI(Player player, String name, Size size) {
        this(player, name, size, null);
    }

    public ItemCreatorSubGUI(Player player, String name, Size size, PanelGUI returnGUI) {
        super(player, "Item Creator %s %s".formatted(Message.ARROW, name), size);

        this.returnGUI = returnGUI == null ? new ItemCreatorGUI(player) : returnGUI;
    }


    @Override
    @OverridingMethodsMustInvokeSuper
    public void onUpdate() {
        super.onUpdate();

        fillItem(0, 8, PANEL_ITEM); // Fill top as well
        setPanelGoBack(PanelSlot.CENTER, returnGUI.getName(), t -> openReturnGUI());
    }

    protected void openReturnGUI() {
        returnGUI.openInventory();
    }

    public boolean willDiscardIfUsedGoBackButton() {
        return false;
    }

    @Override
    public ItemBuilder goBackItemBuilder(String name) {
        final ItemBuilder builder = super.goBackItemBuilder(name);

        if (willDiscardIfUsedGoBackButton()) {
            builder.addTextBlockLore("""
                    
                    &6&lCAREFUL!
                    &eReturning back will discard any changed made.
                    """);
        }

        return builder;
    }

    public final ItemCreator creator() {
        return Main.getItemCreator().getCreator(player);
    }

}
