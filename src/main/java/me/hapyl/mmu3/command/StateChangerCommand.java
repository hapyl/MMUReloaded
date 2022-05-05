package me.hapyl.mmu3.command;

import kz.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.mmu3.Main;
import org.bukkit.entity.Player;

public class StateChangerCommand extends SimplePlayerAdminCommand {
    public StateChangerCommand(String name) {
        super(name);
    }

    @Override
    protected void execute(Player player, String[] strings) {
        Main.getInstance().getStateChanger().giveItem(player);
    }
}
