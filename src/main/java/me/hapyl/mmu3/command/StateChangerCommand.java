package me.hapyl.mmu3.command;

import me.hapyl.eterna.module.util.ArgumentList;
import me.hapyl.mmu3.Main;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class StateChangerCommand extends MMUCommand {

    public StateChangerCommand(@Nonnull String name) {
        super(name);
    }

    @Nonnull
    @Override
    protected String description() {
        return "Gives the state changer item.";
    }

    @Override
    protected void execute(@Nonnull Player player, @Nonnull ArgumentList args) {
        Main.getStateChanger().giveItem(player);
    }

}
