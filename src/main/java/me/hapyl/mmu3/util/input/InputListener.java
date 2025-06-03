package me.hapyl.mmu3.util.input;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.message.Message;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;
import java.util.Arrays;

public interface InputListener {

    @Nonnull
    String name();

    void showUsage(@Nonnull Player player);

    void listen(@Nonnull Player player, @Nonnull InputType input);

    @ApiStatus.NonExtendable
    default void startListening(@Nonnull Player player) {
        Main.getRegistry().inputHandler.listen(player, this);
    }

    @ApiStatus.NonExtendable
    default void stopListening(@Nonnull Player player) {
        Main.getRegistry().inputHandler.unlisten(player);
    }

    static void showUsage(@Nonnull Player player, @Nonnull String usage, @Nonnull InputType... inputs) {
        Arrays.sort(inputs);

        final StringBuilder key = new StringBuilder();

        for (int i = 0; i < inputs.length; i++) {
            key.append(inputs[i]);

            if (i + 1 < inputs.length) {
                key.append(inputs[i + 1].delimiter());
            }
        }

        Message.info(player, "&f&l%s&8 &7&oto %s.".formatted(key, usage));
    }
}
