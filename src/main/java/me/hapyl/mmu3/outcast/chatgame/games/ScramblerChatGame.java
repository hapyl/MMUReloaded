package me.hapyl.mmu3.outcast.chatgame.games;

import me.hapyl.mmu3.outcast.chatgame.ChatGame;
import me.hapyl.mmu3.outcast.chatgame.ChatGameInstance;
import me.hapyl.mmu3.outcast.game.Arguments;
import me.hapyl.spigotutils.module.util.CollectionUtils;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

public class ScramblerChatGame extends ChatGame {

    private final String[] defaultWords =
            {
                    "Mango",
                    "Banana",
                    "Apple",
                    "Orange",
                    "Strawberry",
                    "Watermelon",
                    "Pineapple",
                    "Grape",
                    "Pear",
                    "Peach",
                    "Cherry"
            };

    public ScramblerChatGame() {
        super("Scrambler", "Guess the scrambled word!", TimeUnit.SECONDS.toMillis(60));
    }

    public String scramble(String egg) {
        final char[] chars = egg.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            final int index = (int) (Math.random() * chars.length);
            final char temp = chars[i];

            chars[i] = chars[index];
            chars[index] = temp;
        }

        return new String(chars);
    }

    @Nonnull
    @Override
    public ChatGameInstance newInstance(@Nonnull Arguments args) {
        return new ChatGameInstance(this, scramble(args.getString(0, CollectionUtils.randomElement(defaultWords)))) {

        };
    }
}
