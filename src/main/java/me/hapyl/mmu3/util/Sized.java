package me.hapyl.mmu3.util;

public interface Sized {

    int size();

    default int getSize() {
        return size();
    }

}
