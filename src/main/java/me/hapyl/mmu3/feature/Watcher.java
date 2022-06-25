package me.hapyl.mmu3.feature;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerProfile;

import java.util.Map;

public class Watcher extends Feature {

    private final Map<Player, PlayerProfile> savedProfiles;

    public Watcher(Main mmu3plugin) {
        super(mmu3plugin);
        this.savedProfiles = Maps.newHashMap();
    }

    /**
     * Set skins using texture.
     *
     * @param texture - Url to mojang texture server.
     */
    public void setSkin(String texture) {

    }

    /**
     * Set skin using player's name.
     *
     * @param playerName - Player's name.
     */
    public void setSkinName(String playerName) {
        final Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
        }
    }


}
