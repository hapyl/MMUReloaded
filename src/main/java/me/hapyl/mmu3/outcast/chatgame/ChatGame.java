package me.hapyl.mmu3.outcast.chatgame;

import me.hapyl.mmu3.outcast.game.Arguments;
import me.hapyl.spigotutils.module.chat.Chat;

import javax.annotation.Nonnull;

public abstract class ChatGame {

    private final String name;
    private final String description;
    private final long timeLimit;

    protected ChatGame(String name, String description, long timeLimit) {
        this.name = name;
        this.description = description;
        this.timeLimit = timeLimit;
    }

    public long getTimeLimit() {
        return timeLimit;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Nonnull
    public abstract ChatGameInstance newInstance(@Nonnull Arguments args);

    public void log(String str, Object... format) {
        Chat.broadcast("&b&l" + getName() + "! &a" + Chat.format(str.formatted(format)));
    }

}
