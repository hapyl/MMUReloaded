package me.hapyl.mmu3.command;

import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import org.bukkit.Tag;
import org.bukkit.entity.Player;

// TODO: 030. 30/05/2022
public class BukkitTagCommand extends SimplePlayerAdminCommand {
    public BukkitTagCommand(String name) {
        super(name);
        setDescription("Shows all materials in a tag if exists.");
    }

    @Override
    protected void execute(Player player, String[] args) {
        //
        // bukkittag (Tag)
        // bukkittag (Tag) (value)
        //
        if (args.length == 0) {
            Message.NOT_ENOUGH_ARGUMENTS_EXPECTED_AT_LEAST.send(player, 1);
            return;
        }

        final Tag<?> tag = getTagByName(args[0]);
        if (tag == null) {
            Message.error(player, "%s is invalid tag!", args[0]);
            return;
        }

        if (args.length == 1) {
            Message.info(player, "%s contains %s values:", tag);
        }

    }

    private Tag<?> getTagByName(String name) {
        return null;
    }


}
