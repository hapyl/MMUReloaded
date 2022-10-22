package me.hapyl.mmu3.command;

import me.hapyl.mmu3.outcast.hypixel.slayer.gui.SlayerGUI;
import me.hapyl.spigotutils.module.command.SimplePlayerCommand;
import org.bukkit.entity.Player;

public class SlayerCommand extends SimplePlayerCommand {
    public SlayerCommand(String name) {
        super(name);
        setDescription("Opens Slayer menu.");
    }

    @Override
    protected void execute(Player player, String[] args) {
        new SlayerGUI(player);
    }
}
