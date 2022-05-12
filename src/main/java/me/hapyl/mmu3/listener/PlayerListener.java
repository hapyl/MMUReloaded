package me.hapyl.mmu3.listener;

import me.hapyl.mmu3.PersistentPlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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

}
