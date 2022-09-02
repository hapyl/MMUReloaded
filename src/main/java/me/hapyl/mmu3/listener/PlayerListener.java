package me.hapyl.mmu3.listener;

import me.hapyl.mmu3.PersistentPlayerData;
import me.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler()
    public void handlePlayerJoin(PlayerJoinEvent ev) {
        final Player player = ev.getPlayer();
        PersistentPlayerData.createData(player);
    }

    @EventHandler()
    public void handlePlayerQuit(PlayerQuitEvent ev) {
        final Player player = ev.getPlayer();
        PersistentPlayerData.getData(player).saveData();
    }

    // Handle slot interact for checking slot number.
    @EventHandler()
    public void handleInventoryClickEvent(InventoryClickEvent ev) {
        final ClickType click = ev.getClick();
        final HumanEntity player = ev.getWhoClicked();
        if (click == ClickType.SHIFT_RIGHT && player.isOp()) {
            final int rawSlot = ev.getRawSlot();

            ev.setCancelled(true);
            Chat.sendMessage(player, "&fClicked &l%s&f. &7(%s of %% 9)", rawSlot, rawSlot % 9);
        }
    }

}
