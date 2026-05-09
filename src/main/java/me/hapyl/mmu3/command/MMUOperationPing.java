package me.hapyl.mmu3.command;

import me.hapyl.mmu3.MMULogger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MMUOperationPing implements MMUOperation {

    @Override
    public @NotNull String name() {
        return "ping";
    }

    @Override
    public @NotNull String description() {
        return "Allows to see player ping in milliseconds.";
    }

    @Override
    public void process(@NotNull Player player, @NotNull ArgumentList args) {
        final Player target = args.get(0).toPlayer().orElse(player);
        final int ping = target.getPing();

        MMULogger.info(
                player,
                Component.empty()
                        .append(target.equals(player) ? Component.text("Your") : target.name().append(Component.text("'s")))
                        .append(Component.text(" ping is "))
                        .append(Component.text(ping, NamedTextColor.GREEN))
                        .append(Component.text("ms."))
        );
    }

}