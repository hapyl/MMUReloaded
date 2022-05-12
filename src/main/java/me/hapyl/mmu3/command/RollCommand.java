package me.hapyl.mmu3.command;

import kz.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.mmu3.feature.Dice;
import org.bukkit.entity.Player;

public class RollCommand extends SimplePlayerAdminCommand {

    public RollCommand(String name) {
        super(name);
        setDescription("Throws a dice to roll a random number between 1-6.");
        setAliases("dice");
        setCooldownTick(40);
    }

    @Override
    protected void execute(Player player, String[] args) {
        new Dice(player);
    }

}
