package me.hapyl.mmu3.feature.lastlocation;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.utils.InjectListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;

public class LastLocationListener extends InjectListener {

    public LastLocationListener(Main main) {
        super(main);
    }

    @EventHandler(ignoreCancelled = true)
    public void handlePlayerTeleportEvent(PlayerTeleportEvent ev) {
        final Player player = ev.getPlayer();

        Main.getLastLocation().saveLastLocation(player.getUniqueId(), ev.getFrom());
    }

}
