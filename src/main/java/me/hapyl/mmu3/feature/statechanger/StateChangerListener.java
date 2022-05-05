package me.hapyl.mmu3.feature.statechanger;

import kz.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.mmu3.Main;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class StateChangerListener implements Listener {

    @EventHandler()
    public void handlePlayerInteractEvent(PlayerInteractEvent ev) {
        final Player player = ev.getPlayer();
        final ItemStack item = ev.getItem();
        final Action action = ev.getAction();
        final Block clickedBlock = ev.getClickedBlock();

        if (clickedBlock == null
                || action == Action.PHYSICAL
                || item == null
                || !ItemBuilder.itemContainsId(item, "state_changer")) {
            return;
        }

        ev.setCancelled(true);
        stateChanger().openEditor(player, clickedBlock);

    }

    // temp for testing
    @EventHandler()
    public void handleInventoryClickEvent(InventoryClickEvent ev) {
        if (ev.getClick() == ClickType.MIDDLE && ev.getWhoClicked().isOp()) {
            final int rawSlot = ev.getRawSlot();
            ev.getWhoClicked().sendMessage("Clicked " + ChatColor.BOLD + rawSlot);
        }
    }

    public StateChanger stateChanger() {
        return Main.getInstance().getStateChanger();
    }

}
