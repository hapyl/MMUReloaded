package me.hapyl.mmu3.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class SealedMap<K, V, S> extends HashMap<K, V> {

    private boolean sealed = true;
    private S seal = null;

    public final void seal(S s) throws IllegalStateException {
        checkSeal();
        sealed = true;
        seal = s;
    }

    public final void unseal(S s) {
        if (!seal.equals(s)) {
            throw new IllegalArgumentException("wrong or null seal");
        }
        seal = null;
        sealed = false;
    }

    public boolean isSealed() {
        return sealed;
    }

    @Override
    public V get(Object key) {
        checkSeal();
        return super.get(key);
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        checkSeal();
        return super.getOrDefault(key, defaultValue);
    }

    @Override
    public V put(K key, V value) {
        checkSeal();
        return super.put(key, value);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        checkSeal();
        return super.putIfAbsent(key, value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        checkSeal();
        super.putAll(m);
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        checkSeal();
        return super.compute(key, remappingFunction);
    }

    private void checkSeal() {
        if (sealed && seal != null) {
            throw new IllegalStateException("cannot modify sealed map");
        }
    }
}
