package me.hapyl.mmu3.command;

import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.test.PlayerShader;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerShaderCommand extends SimplePlayerAdminCommand {
    public PlayerShaderCommand(String name) {
        super(name);
        setUsage("shader <shader> [player]");
    }

    @Override
    protected void execute(Player player, String[] args) {
        if (args.length < 1) {
            sendInvalidUsageMessage(player);
            return;
        }

        final PlayerShader.ShaderType shader = Validate.getEnumValue(PlayerShader.ShaderType.class, args[0]);
        final Player target = args.length > 1 ? Bukkit.getPlayer(args[1]) : player;

        if (shader == null) {
            Message.error(player, "Invalid shader.");
            return;
        }

        if (target == null) {
            Message.PLAYER_NOT_ONLINE.send(player, args[1]);
            return;
        }

        PlayerShader.set(target, shader);
        Message.success(player, "Set shader of %s to %s.", target.getName(), shader.name());
    }
}
