package me.hapyl.mmu3.feature.entityeditor;

import me.hapyl.mmu3.utils.PanelGUI;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class EntityEditorGUI extends PanelGUI {

    private final Entity entity;

    public EntityEditorGUI(Player player, Entity entity) {
        super(player, "Entity Editor &4&lBASIC", Size.FOUR);
        this.entity = entity;
    }

    @Override
    public void updateInventory() {


        openInventory();
    }
}
