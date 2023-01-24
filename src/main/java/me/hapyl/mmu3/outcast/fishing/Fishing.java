package me.hapyl.mmu3.outcast.fishing;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;
import me.hapyl.spigotutils.module.player.PlayerLib;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import javax.annotation.Nonnull;
import java.util.Map;

public class Fishing extends Feature implements Listener {

    private final Map<Player, FishData> playerFishDataMap;

    public Fishing(Main mmu3plugin) {
        super(mmu3plugin);

        playerFishDataMap = Maps.newHashMap();
    }

    @Nonnull
    public FishData getPlayerData(Player player) {
        return playerFishDataMap.computeIfAbsent(player, s -> new FishData(player));
    }

    @EventHandler()
    public void handleFish(PlayerFishEvent ev) {
        if (!this.isEnabled()) {
            return;
        }

        final PlayerFishEvent.State state = ev.getState();
        final Player player = ev.getPlayer();

        switch (state) {
            // Player started fishing, apply internal data such as baits, luck etc.
            case FISHING -> {
                playerFishDataMap.put(player, new FishData(player));
            }

            // Player caught fish, start fishing UI.
            case CAUGHT_FISH -> {
                // TODO -> Calculate fish

                final FishData fishData = getPlayerData(player);
                fishData.setFish(FishType.random());

                new ReelUI(fishData);

                ev.setCancelled(true);
                ev.getHook().remove();
            }

            // Fish bit, play animation.
            case BITE -> {
                final FishHook hook = ev.getHook();
                final Location location = hook.getLocation();

                // TODO -> Add customization for sound and particles.
                PlayerLib.spawnParticle(location, Particle.CRIT, 3, 0, 0, 0, 0.03f);
                PlayerLib.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 1);
            }
        }
    }
}
