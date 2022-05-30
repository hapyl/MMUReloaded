package me.hapyl.mmu3.command;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.Message;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import org.bukkit.entity.Player;

public class CalculateCommand extends SimplePlayerAdminCommand {
    public CalculateCommand(String name) {
        super(name);
        setAliases("calc", "eval");
        setDescription("Evaluates mathematical expression.");
    }

    @Override
    protected void execute(Player player, String[] args) {
        if (args.length == 0) {
            Message.NOT_ENOUGH_ARGUMENTS_EXPECTED_AT_LEAST.send(player, 1);
            return;
        }

        Main.getCalculate().buildStringAndEvaluate(player, args);
    }
}
