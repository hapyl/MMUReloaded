package me.hapyl.mmu3.utils;

public interface Sized {

    int size();

    default int getSize() {
        return size();
    }

}
