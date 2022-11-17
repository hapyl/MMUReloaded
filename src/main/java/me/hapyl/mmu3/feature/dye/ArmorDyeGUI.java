package me.hapyl.mmu3.feature.dye;

import me.hapyl.mmu3.utils.PanelGUI;
import org.bukkit.entity.Player;

public class ArmorDyeGUI extends PanelGUI {

    private final ArmorData data;

    public ArmorDyeGUI(Player player, ArmorData data) {
        super(player, "Dyeing", Size.FOUR);
        this.data = data;
    }

    @Override
    public void updateInventory() {

    }
}
