package me.hapyl.mmu3.command;

import me.hapyl.mmu3.Message;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Lockable;
import org.bukkit.entity.Player;

public class LockCommand extends SimplePlayerAdminCommand {
    public LockCommand(String name) {
        super(name);
        setDescription("Allows to lock a container with a string.");
    }

    @Override
    protected void execute(Player player, String[] args) {
        final String lock = args.length >= 1 ? args[0] : randomLock();
        final Block targetBlock = player.getTargetBlockExact(10);

        if (targetBlock == null) {
            Message.error(player, "You must look at a block to use this!");
            return;
        }

        final BlockState state = targetBlock.getState();
        final String blockName = Chat.capitalize(targetBlock.getType());

        if (state instanceof Lockable lockable) {
            if (lockable.isLocked()) {
                lockable.setLock(null);
                Message.info(player, "Unlocked %s.", blockName);
            }
            else {
                lockable.setLock(lock);
                Message.info(player, "Locked %s with code '%s'.", blockName, lock);
            }

            ((BlockState) lockable).update(false, false);
            return;
        }

        Message.error(player, "This block cannot be locked!");

    }

    private String randomLock() {
        return RandomStringUtils.random(10, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
    }

}
