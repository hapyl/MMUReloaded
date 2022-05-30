package me.hapyl.mmu3.feature.standeditor;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.Message;
import me.hapyl.mmu3.feature.Feature;
import me.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

public class StandEditor extends Feature {

    private final Map<Player, Data> dataMap;
    private final Map<UUID, Map<Integer, StandInfo>> loadoutMap;
    private final Map<UUID, TuneData> tuneDataMap;
    private final Map<ArmorStand, Data> editingMap;
    private final Material tuningMaterial;

    private final Map<Integer, TuneData.Part> tuningPartSlotMap;

    public StandEditor(Main mmu3plugin) {
        super(mmu3plugin);
        dataMap = Maps.newHashMap();
        tuneDataMap = Maps.newHashMap();
        editingMap = Maps.newHashMap();
        loadoutMap = Maps.newHashMap();
        tuningMaterial = Material.FLINT;

        // init tuning slots
        tuningPartSlotMap = Maps.newHashMap();
        tuningPartSlotMap.put(11, TuneData.Part.HEAD);
        tuningPartSlotMap.put(20, TuneData.Part.BODY);
        tuningPartSlotMap.put(13, TuneData.Part.LEFT_ARM);
        tuningPartSlotMap.put(22, TuneData.Part.RIGHT_ARM);
        tuningPartSlotMap.put(15, TuneData.Part.LEFT_LEG);
        tuningPartSlotMap.put(24, TuneData.Part.RIGHT_LEG);
    }

    @Nullable
    public Data getData(Player player) {
        return dataMap.get(player);
    }

    public void tuneStand(Player player, ArmorStand stand, TuneData data, double step) {
        final double[] amount = data.getAxis().scaleIfAxis(step);
        final double x = amount[0];
        final double y = amount[1];
        final double z = amount[2];

        final TuneData.Part part = data.getPart();
        final double nonZero = x != 0.0d ? x : y != 0.0d ? y : z;

        switch (part) {
            case HEAD -> stand.setHeadPose(stand.getHeadPose().add(x, y, z));
            case BODY -> stand.setBodyPose(stand.getBodyPose().add(x, y, z));
            case LEFT_ARM -> stand.setLeftArmPose(stand.getLeftArmPose().add(x, y, z));
            case RIGHT_ARM -> stand.setRightArmPose(stand.getRightArmPose().add(x, y, z));
            case LEFT_LEG -> stand.setLeftLegPose(stand.getLeftLegPose().add(x, y, z));
            case RIGHT_LEG -> stand.setRightLegPose(stand.getRightLegPose().add(x, y, z));
        }

        // Using title to not spam the chat
        Chat.sendActionbar(player, "&6%s &f%s &l%s Axis", part.getName(), nonZero, data.getAxis().name());
        Message.sound(player, Sound.BLOCK_NOTE_BLOCK_HAT, player.isSneaking() ? 1.25f : 1.0f);
    }

    @Nullable
    public TuneData getTuneData(Player player) {
        return tuneDataMap.get(player.getUniqueId());
    }

    public TuneData getTuneDataOrNew(Player player) {
        return tuneDataMap.computeIfAbsent(player.getUniqueId(), p -> new TuneData());
    }

    public void setData(Player player, Data data) {
        dataMap.put(player, data);
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


    public boolean isEditable(ArmorStand stand) {
        return true;
    }

    public void saveLoadout(int slot, Data data) {
        final Map<Integer, StandInfo> map = loadoutMap.computeIfAbsent(data.getPlayer().getUniqueId(), a -> Maps.newHashMap());
        map.put(slot, new StandInfo(data.getStand()));
        loadoutMap.put(data.getPlayer().getUniqueId(), map);
    }

    @Nullable
    public StandInfo getLoadout(Player player, int slot) {
        final Map<Integer, StandInfo> map = loadoutMap.getOrDefault(player.getUniqueId(), Maps.newHashMap());
        return map.get(slot);
    }

    public String getLoadoutName(Player player, int slot) {
        final StandInfo loadout = getLoadout(player, slot);
        return loadout == null ? "&7None!" : loadout.getName();
    }

    public void setTuneData(Player player, TuneData data) {
        tuneDataMap.put(player.getUniqueId(), data);
    }

    public boolean isEditingItem(ItemStack item) {
        return item != null && item.getType() == tuningMaterial;
    }

    public boolean isTaken(ArmorStand stand) {
        return editingMap.containsKey(stand);
    }

    public Map<Integer, TuneData.Part> getTuningPartSlotMap() {
        return tuningPartSlotMap;
    }

    public void exitMoveMode(Data data) {
        data.setWaitForMove(false);
        new StandEditorGUI(data.getPlayer(), data);
        Message.success(data.getPlayer(), "Exited move mode!");
    }

    public void setTaken(Data data, boolean flag) {
        final ArmorStand stand = data.getStand();
        final Player player = data.getPlayer();
        if (flag) {
            dataMap.put(player, data);
            editingMap.put(stand, data);
        }
        else {
            dataMap.remove(player);
            editingMap.remove(stand);
        }
    }

    public String getTakerName(ArmorStand stand) {
        final Player editor = editingMap.get(stand).getPlayer();
        return editor == null ? "None" : editor.getName();
    }

    public void checkStandRemoval(ArmorStand stand, Entity killer) {
        if (editingMap.containsKey(stand)) {
            final Data data = editingMap.get(stand);
            final Player player = data.getPlayer();

            data.setTaken(false);
            player.closeInventory();
            Message.error(player, "An armor stand you were editing was destroyed%s!", killer == null ? "" : " by " + killer.getName());
        }
    }
}
