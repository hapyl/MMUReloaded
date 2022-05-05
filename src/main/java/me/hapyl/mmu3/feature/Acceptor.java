package me.hapyl.mmu3.feature;

public interface Acceptor<T, R> {

    R get(T t);

}
