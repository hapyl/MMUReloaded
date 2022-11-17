package me.hapyl.mmu3.command;

import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;

public class WatcherCommand extends SimplePlayerAdminCommand {
    public WatcherCommand(String name) {
        super(name);
        setAliases("w");
    }

    @Override
    protected void execute(Player player, String[] args) {
        final PlayerProfile profile = player.getPlayerProfile();

        try {
            Chat.sendMessage(player, "&aUpdating skin test.");

            final PlayerTextures textures = profile.getTextures();
            textures.setSkin(new URL("http://textures.minecraft.net/texture/e17cb11a339366b60ace013f42b1ab5837ecf12a4985552745d84aac80bfafa8"));

            profile.update().thenRun(() -> {
                Chat.sendMessage(player, "&aUpdated.");
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


    }
}
