package me.hapyl.mmu3.command;

import com.google.common.collect.Maps;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.util.Map;

public class SkinCommand extends SimplePlayerAdminCommand {

    private final Map<Player, PlayerProfile> storedProfile = Maps.newHashMap();

    public SkinCommand(String name) {
        super(name);
    }

    @Override
    protected void execute(Player player, String[] args) {
        // skin (skin-owner)
        final PlayerProfile profile = player.getPlayerProfile();
        final PlayerTextures textures = profile.getTextures();

        storedProfile.putIfAbsent(player, profile.clone());
    }
}
