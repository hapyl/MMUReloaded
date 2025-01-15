package me.hapyl.mmu3.command;

import me.hapyl.eterna.module.command.SimpleCommand;
import me.hapyl.eterna.module.util.ArgumentList;
import me.hapyl.mmu3.message.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public abstract class MMUCommand extends SimpleCommand {

    public MMUCommand(@Nonnull String name) {
        super(name);

        setDescription(description());
        setAliases(Objects.requireNonNullElse(aliases(), new String[] {}));
        setUsage(Objects.requireNonNullElse(usage(), name));
    }

    @Nonnull
    protected abstract String description();

    @Nullable
    protected String usage() {
        return null;
    }

    @Nullable
    protected String[] aliases() {
        return null;
    }

    protected abstract void execute(@Nonnull Player player, @Nonnull ArgumentList args);

    @Override
    protected void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            Message.error(sender, "You must be a player to execute this command!");
            return;
        }

        if (!player.isOp()) {
            Message.error(player, "You don't have permissions to execute this command!");
            return;
        }

        execute(player, new ArgumentList(args));
    }
}
