package me.hapyl.mmu3.command;

import me.hapyl.mmu3.feature.Dice;
import me.hapyl.eterna.module.command.SimplePlayerAdminCommand;
import org.bukkit.entity.Player;

public class RollCommand extends SimplePlayerAdminCommand {

    public RollCommand(String name) {
        super(name);
        setDescription("Throws a die to roll a random number between 1-6.");
        setAliases("dice");
        setCooldownTick(40);
    }

    @Override
    protected void execute(Player player, String[] args) {
        new Dice(player);
    }

}
