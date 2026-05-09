package me.hapyl.mmu3.command;

import me.hapyl.mmu3.Main;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MMUOperationStateChanger implements MMUOperation {

    @Override
    public @NotNull String name() {
        return "state_changer";
    }

    @Override
    public @NotNull String description() {
        return "Gives the state changer item.";
    }

    @Override
    public void process(@NotNull Player player, @NotNull ArgumentList args) {
        Main.featureRegistry().stateChanger.giveItem(player);
    }

}
