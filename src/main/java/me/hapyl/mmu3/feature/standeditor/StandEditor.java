package me.hapyl.mmu3.feature.standeditor;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;
import me.hapyl.mmu3.message.Message;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.Map;

public class StandEditor extends Feature {

    private final Map<Player, StandEditorData> dataMap;

    public StandEditor(Main mmu3plugin) {
        super(mmu3plugin);
        dataMap = Maps.newHashMap();
    }

    @Nonnull
    public StandEditorData getData(Player player) {
        return dataMap.computeIfAbsent(player, StandEditorData::new);
    }

    public boolean isLocked(ArmorStand stand) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            for (ArmorStand.LockType value : ArmorStand.LockType.values()) {
                if (!stand.hasEquipmentLock(slot, value)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setLock(ArmorStand stand, boolean flag) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            for (ArmorStand.LockType value : ArmorStand.LockType.values()) {
                if (flag) {
                    stand.addEquipmentLock(slot, value);
                }
                else {
                    stand.removeEquipmentLock(slot, value);
                }
            }
        }
    }

    public boolean isTaken(ArmorStand stand) {
        return dataMap.values()
                .stream()
                .anyMatch(data -> data.standOrNull() == stand);
    }

    public String getTakerName(ArmorStand stand) {
        return dataMap.values()
                .stream()
                .filter(data -> data.standOrNull() == stand)
                .findFirst()
                .map(data -> data.player().getName())
                .orElse("None");
    }

    public void checkStandRemoval(ArmorStand stand, Entity killer) {
        final Iterator<StandEditorData> iterator = dataMap.values().iterator();

        while (iterator.hasNext()) {
            final StandEditorData data = iterator.next();

            if (data.standOrNull() != stand) {
                continue;
            }

            Message.error(data.player(), "An armor stand you were editing was destroyed%s!", killer == null ? "" : " by " + killer.getName());

            data.edit(null);
            data.player().closeInventory();
            iterator.remove();
            return;
        }
    }

    public void remove(@Nonnull Player player) {
        dataMap.remove(player);
    }
}
