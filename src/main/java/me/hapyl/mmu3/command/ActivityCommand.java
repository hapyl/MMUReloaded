package me.hapyl.mmu3.command;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.activity.Activity;
import me.hapyl.mmu3.feature.activity.WorldActivity;
import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.util.Validate;
import org.bukkit.entity.Player;

public class ActivityCommand extends SimplePlayerAdminCommand {

    public ActivityCommand(String name) {
        super(name);
        setDescription("Changes world activity status.");

        addCompleterValues(1, Activity.values());
        //addCompleterHandler(1, "&a&nWill toggle: {}", "&c&nInvalid activity!");

        addCompleterHandler(1, (player, arg) -> {
            final Activity activity = Validate.getEnumValue(Activity.class, arg);
            if (activity == null) {
                return "&c&nInvalid activity!";
            }

            final boolean enabled = Main.getRegistry().worldActivity.isEnabled(activity);
            return "&a&nWill &l%s &a&n%s".formatted(enabled ? "disable" : "enable", activity.getName());
        });


    }

    @Override
    protected void execute(Player player, String[] args) {
        // activity - Opens GUI
        // activity (Name)

        if (args.length == 0) {
            Message.error(player, "GUI not implemented yet.");
            return;
        }

        final Activity activity = Validate.getEnumValue(Activity.class, args[0]);
        if (activity == null) {
            Message.error(player, "Invalid activity.");
            return;
        }

        final WorldActivity worldActivity = Main.getRegistry().worldActivity;
        worldActivity.toggleActivity(activity, true);
    }
}
