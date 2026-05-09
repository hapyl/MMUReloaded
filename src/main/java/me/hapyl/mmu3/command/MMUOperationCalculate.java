package me.hapyl.mmu3.command;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.MMULogger;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MMUOperationCalculate implements MMUOperation {

    @Override
    public @NotNull String name() {
        return "calculate";
    }

    @Override
    public @NotNull String description() {
        return "Calculates mathematical expression.";
    }

    @Override
    public void process(@NotNull Player player, @NotNull ArgumentList args) {
        if (args.length == 0) {
            MMULogger.error(player, Component.text("Nothing to calculate!"));
            return;
        }

        Main.getCalculate().buildStringAndEvaluate(player, args);
    }

}
