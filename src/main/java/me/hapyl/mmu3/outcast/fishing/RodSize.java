package me.hapyl.mmu3.outcast.fishing;

public enum RodSize {
    SMALL(1),
    NORMAL(3),
    BIG(5);

    private final int size;

    RodSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
