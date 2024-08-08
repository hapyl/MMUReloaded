package me.hapyl.mmu3.command;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.activity.WorldActivity;
import me.hapyl.eterna.module.command.SimplePlayerAdminCommand;
import org.bukkit.entity.Player;

public class ActivityCommand extends SimplePlayerAdminCommand {

    public ActivityCommand(String name) {
        super(name);

        setAliases("sbp", "blockUpdates");
        setDescription("Toggles block updates.");
    }

    @Override
    protected void execute(Player player, String[] args) {
        final WorldActivity activity = Main.getRegistry().worldActivity;

        activity.toggleActivity(true);
    }

}
