package me.hapyl.mmu3.command;

import me.hapyl.mmu3.Message;
import me.hapyl.mmu3.feature.PlayerPacket;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.util.Enums;
import me.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PacketCommand extends SimplePlayerAdminCommand {

    public PacketCommand(String name) {
        super(name);
        addCompleterValues(1, Enums.getValuesNames(PlayerPacket.class));
        setDescription("Allows to send 'fun' packets to players.");
        setAliases("pk");
    }

    @Override
    protected void execute(Player player, String[] args) {
        // packet (Packet) [Player]
        if (args.length == 0) {
            Message.NOT_ENOUGH_ARGUMENTS_EXPECTED_AT_LEAST.send(player, 1);
            return;
        }

        final PlayerPacket packet = Validate.getEnumValue(PlayerPacket.class, args[0]);
        final Player target = args.length > 1 ? Bukkit.getPlayer(args[1]) : player;

        if (packet == null) {
            Message.INVALID_ARGUMENT.send(player, args[0]);
            return;
        }

        if (target == null) {
            Message.PLAYER_NOT_ONLINE.send(player, args[1]);
            return;
        }

        packet.sendPacket(target);
        Message.info(player, "Send %s packet to %s.", Chat.capitalize(packet), player == target ? "yourself" : target.getName());

    }

}
