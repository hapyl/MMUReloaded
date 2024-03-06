package me.hapyl.mmu3.feature;

public interface MMURunnable extends Runnable {

    default int delay() {
        return 0;
    }

    default int period() {
        return 1;
    }

    default boolean async() {
        return false;
    }

}
