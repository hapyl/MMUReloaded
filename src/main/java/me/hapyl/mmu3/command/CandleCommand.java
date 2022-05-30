package me.hapyl.mmu3.command;

import me.hapyl.mmu3.feature.candle.CandleGUI;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import org.bukkit.entity.Player;

public class CandleCommand extends SimplePlayerAdminCommand {
    public CandleCommand(String name) {
        super(name);
    }

    @Override
    protected void execute(Player player, String[] args) {
        new CandleGUI(player);
    }
}
