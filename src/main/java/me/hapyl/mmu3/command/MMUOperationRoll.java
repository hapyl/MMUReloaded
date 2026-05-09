package me.hapyl.mmu3.command;

import me.hapyl.mmu3.feature.DiceRoll;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MMUOperationRoll implements MMUOperation {

    @Override
    public @NotNull String name() {
        return "roll";
    }

    @Override
    public @NotNull String description() {
        return "Rolls a d6 die.";
    }

    @Override
    public void process(@NotNull Player player, @NotNull ArgumentList args) {
        new DiceRoll(player);
    }

}
