package me.hapyl.mmu3.command;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class BackCommand extends SimplePlayerAdminCommand {
    public BackCommand(String name) {
        super(name);
        setDescription("Teleports player to their last saved location if such present.");
    }

    @Override
    protected void execute(Player player, String[] args) {
        final Location location = Main.getLastLocation().getLastLocation(player.getUniqueId());

        if (location == null) {
            Message.error(player, "You don't have location saved.");
            return;
        }

        player.teleport(location);
        Message.success(player, "Teleported to last location!");
        Message.sound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f);
    }
}
