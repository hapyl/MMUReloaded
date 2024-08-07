package me.hapyl.mmu3.command;

import me.hapyl.mmu3.message.Message;
import me.hapyl.eterna.module.command.SimplePlayerAdminCommand;
import me.hapyl.eterna.module.player.PlayerLib;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SelfTeleportCommand extends SimplePlayerAdminCommand {
    public SelfTeleportCommand(String name) {
        super(name);
        setAliases("s");
        setDescription("Teleports players to you.");
    }

    @Override
    protected void execute(Player player, String[] args) {
        if (args.length != 1) {
            Message.NOT_ENOUGH_ARGUMENTS_EXPECTED.send(player, 1);
            return;
        }

        final Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            Message.PLAYER_NOT_ONLINE.send(player, args[0]);
            return;
        }

        if (target == player) {
            Message.error(player, "Tell me at least a single reason for you to do this?");
            return;
        }

        target.teleport(player);
        Message.success(player, "Teleported %s to you!", target.getName());
        Message.info(target, "%s teleported you to them!", player.getName());
        PlayerLib.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f);
        PlayerLib.playSound(target, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f);
    }
}
