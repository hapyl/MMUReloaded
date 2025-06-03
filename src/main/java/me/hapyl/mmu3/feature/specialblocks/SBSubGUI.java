package me.hapyl.mmu3.feature.specialblocks;

import me.hapyl.mmu3.util.PanelGUI;
import org.bukkit.entity.Player;

import java.util.Collection;

public class SBSubGUI extends PanelGUI {

    private final Collection<SpecialBlock> blocks;

    public SBSubGUI(Player player, String subName, Size size, Collection<SpecialBlock> blocks) {
        super(player, menuArrowSplit("Special Blocks", subName), size);
        this.blocks = blocks;

        openInventory();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        setPanelGoBack("Special Blocks", SpecialBlocksGUI::new);
        setPanelCloseMenu();

        for (SpecialBlock block : blocks) {
            final int slot = block.getSlot();

            setItem(slot, block.getIcon());
            setAction(slot, block::giveItem);
        }
    }

}
