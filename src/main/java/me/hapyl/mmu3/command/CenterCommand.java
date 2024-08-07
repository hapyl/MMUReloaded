package me.hapyl.mmu3.command;

import me.hapyl.mmu3.message.Message;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.chat.LazyClickEvent;
import me.hapyl.eterna.module.chat.LazyHoverEvent;
import me.hapyl.eterna.module.command.SimplePlayerAdminCommand;
import me.hapyl.eterna.module.util.BukkitUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class CenterCommand extends SimplePlayerAdminCommand {
    public CenterCommand(String name) {
        super(name);
        setDescription("Centers you at your current block.");
    }

    @Override
    protected void execute(Player player, String[] args) {
        final Location location = player.getLocation();
        final Location centerLocation = new Location(
                location.getWorld(),
                location.getBlockX() + 0.5d,
                location.getY(),
                location.getBlockZ() + 0.5d,
                location.getYaw(),
                location.getPitch()
        );

        player.teleport(centerLocation);
        Message.success(player, "Centered you at %s.", BukkitUtils.locationToString(centerLocation));
        Message.sound(player, Sound.ENTITY_ENDERMAN_TELEPORT);

        sendMessage(player, centerLocation, "minecraft command", "/tp @s %s %s %s");
        sendMessage(player, centerLocation, "spigot code", "%sd, %sd, %sd");
    }

    private void sendMessage(Player player, Location location, String message, String toCopy) {
        Chat.sendClickableHoverableMessage(
                player,
                LazyClickEvent.SUGGEST_COMMAND.of(toCopy.formatted(location.getX(), location.getY(), location.getZ())),
                LazyHoverEvent.SHOW_TEXT.of("&7Click to copy %s.".formatted(message)),
                "%s &e&lCLICK &7to copy %s.".formatted(Message.PREFIX, message)
        );
    }
}
