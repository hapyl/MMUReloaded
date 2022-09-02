package me.hapyl.mmu3.game;

import me.hapyl.mmu3.utils.PanelGUI;
import org.bukkit.entity.Player;

public class GameSelectGUI extends PanelGUI {
    public GameSelectGUI(Player player) {
        super(player, "Game Selection", Size.FOUR);
        updateInventory();
    }

    @Override
    public void updateInventory() {
        openInventory();
    }
}
