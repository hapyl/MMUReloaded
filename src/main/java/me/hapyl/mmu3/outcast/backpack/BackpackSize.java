package me.hapyl.mmu3.outcast.backpack;

import me.hapyl.spigotutils.module.chat.Chat;

public enum BackpackSize {

    SMALL(1),
    MEDIUM(2),
    LARGE(3),
    GREATER(4),
    JUMBO(5);

    private final int size;

    BackpackSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public int getSizeScaled() {
        return size * 9;
    }

    public String getName() {
        return Chat.capitalize(this);
    }
}
