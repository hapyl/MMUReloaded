package me.hapyl.mmu3.feature.entityeditor;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class BasicEntityEditor extends Feature {

    private final Map<UUID, Entity> editingMap = Maps.newHashMap();

    public BasicEntityEditor(Main mmu3plugin) {
        super(mmu3plugin);
    }

    public void openEditor(Player player, Entity entity) {

    }

    public boolean isBeingEdited(Entity entity) {
        return false;
    }

}
