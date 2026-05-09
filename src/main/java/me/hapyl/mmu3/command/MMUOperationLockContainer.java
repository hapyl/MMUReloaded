package me.hapyl.mmu3.command;

import me.hapyl.mmu3.MMULogger;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Lockable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class MMUOperationLockContainer implements MMUOperation {

    private static final Random RANDOM = new Random();
    private static final String RANDOM_LOCK_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private static final int RANDOM_LOCK_LENGTH = 10;

    @Override
    public @NotNull String name() {
        return "lock_container";
    }

    @Override
    public @NotNull String description() {
        return "Toggles container lock of the target container.";
    }

    @SuppressWarnings("deprecation" /* I'm confused about the new lock system, why comment `lock` data component? */)
    @Override
    public void process(@NotNull Player player, @NotNull ArgumentList args) {
        final Block targetBlock = player.getTargetBlockExact(10);

        if (targetBlock == null) {
            MMULogger.error(player, Component.text("You must look at a block to use this!"));
            return;
        }

        final BlockState state = targetBlock.getState();

        if (!(state instanceof Lockable lockable)) {
            MMULogger.error(player, Component.text("This block cannot be locked!"));
            return;
        }

        if (lockable.isLocked()) {
            lockable.setLock(null);

            MMULogger.success(
                    player,
                    Component.empty()
                            .append(Component.text("Unlocked "))
                            .append(Component.translatable(targetBlock.translationKey()))
                            .append(Component.text("!"))
            );
        }
        else {
            final String lock = args.length > 0 ? args.get(0).toString() : randomLock();

            lockable.setLock(lock);

            MMULogger.success(
                    player,
                    Component.empty()
                            .append(Component.text("Locked "))
                            .append(Component.translatable(targetBlock.translationKey()))
                            .append(Component.text(" with code "))
                            .append(Component.text(lock))
                            .append(Component.text("!"))
            );
        }

    }

    @NotNull
    private static String randomLock() {
        final char[] chars = new char[RANDOM_LOCK_LENGTH];

        for (int i = 0; i < chars.length; i++) {
            chars[i] = RANDOM_LOCK_CHARS.charAt(RANDOM.nextInt(RANDOM_LOCK_CHARS.length()));
        }

        return new String(chars);
    }

}
