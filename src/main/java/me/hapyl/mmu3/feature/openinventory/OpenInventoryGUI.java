package me.hapyl.mmu3.feature.openinventory;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.Message;
import me.hapyl.mmu3.utils.PanelGUI;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.inventory.gui.CancelType;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class OpenInventoryGUI extends PanelGUI {

    private static final ItemStack ICON_SPLIT = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)
            .setName("&a↑ Inventory")
            .addLore("&a↓ Hotbar")
            .build();

    private static final ItemStack ICON_SPLIT_ARMOR_RIGHT = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)
            .setName("&a→ Armor")
            .build();

    private static final ItemStack ICON_SPLIT_ARMOR_LEFT_HAND_RIGHT = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)
            .setName("&a← Armor")
            .addLore("&a→ Hand Items")
            .build();

    private static final ItemStack ICON_SPLIT_HAND_LEFT = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)
            .setName("&a← Hand Items")
            .build();

    private static final Map<EquipmentSlot, Integer> equipmentSlotMap = Maps.newHashMap();

    static {
        equipmentSlotMap.put(EquipmentSlot.HEAD, 46);
        equipmentSlotMap.put(EquipmentSlot.CHEST, 47);
        equipmentSlotMap.put(EquipmentSlot.LEGS, 48);
        equipmentSlotMap.put(EquipmentSlot.FEET, 49);
        equipmentSlotMap.put(EquipmentSlot.HAND, 51);
        equipmentSlotMap.put(EquipmentSlot.OFF_HAND, 52);
    }

    private final Player target;

    public OpenInventoryGUI(Player player, Player target) {
        super(player, target.getName() + "'s Inventory", Size.NO_PANEL);
        this.target = target;
        setCancelType(CancelType.NEITHER);
        setEventListener((p, gui, ev) -> {
            final int slot = ev.getRawSlot();
            // dummy slots check since CancelType = NEITHER
            if ((slot >= 27 && slot <= 35) || (slot == 45 || slot == 50 || slot == 53)) {
                ev.setCancelled(true);
            }
        });
        setCloseEvent(t -> updateTargetInventory());
        updateInventory();
        openInventory();
    }

    @Override
    public void openInventory() {
        super.openInventory();
        Message.sound(getPlayer(), Sound.BLOCK_CHEST_OPEN, 2.0f);
    }

    @Override
    public void updateInventory() {
        fillItem(27, 35, ICON_SPLIT);
        fillItem(45, 53, PANEL_ITEM);
        setItem(45, ICON_SPLIT_ARMOR_RIGHT);
        setItem(50, ICON_SPLIT_ARMOR_LEFT_HAND_RIGHT);
        setItem(53, ICON_SPLIT_HAND_LEFT);

        final PlayerInventory inventory = target.getInventory();

        // Hotbar
        for (int i = 0, slot = 36; i < 9; i++, slot++) {
            setItem(slot, nonNullItem(inventory.getItem(i)));
        }

        // Inventory
        for (int i = 0; i < 27; i++) {
            setItem(i, nonNullItem(inventory.getItem(i + 9)));
        }

        // Armor and Hand Items
        equipmentSlotMap.forEach((slot, i) -> setItem(i, nonNullItem(inventory.getItem(slot))));
    }

    public void updateTargetInventory() {
        final PlayerInventory inventory = target.getInventory();
        inventory.clear();
        final Inventory guiInventory = getInventory();

        // Fill Inventory
        for (int i = 0; i < 27; i++) {
            final ItemStack item = guiInventory.getItem(i);
            if (item == null) {
                continue;
            }
            inventory.setItem(i + 9, item);
        }

        // Fill Hotbar
        for (int i = 0, slot = 36; i < 9; i++, slot++) {
            final ItemStack item = guiInventory.getItem(slot);
            if (item == null) {
                continue;
            }

            inventory.setItem(i, item);
        }

        // Fill Armor and Hand Items
        equipmentSlotMap.forEach((slot, i) -> {
            final ItemStack item = guiInventory.getItem(i);
            if (item == null) {
                return;
            }

            inventory.setItem(slot, item);
        });

        // update target's inventory 1 tick later
        new BukkitRunnable() {
            @Override
            public void run() {
                target.updateInventory();
            }
        }.runTaskLater(Main.getInstance(), 1L);
    }

    private ItemStack nonNullItem(ItemStack stack) {
        return (stack == null) ? new ItemStack(Material.AIR) : stack;
    }


}
