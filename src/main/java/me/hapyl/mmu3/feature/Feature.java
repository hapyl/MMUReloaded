package me.hapyl.mmu3.feature;

import me.hapyl.mmu3.Main;

public class Feature {

    private final Main mmu3plugin;

    public Feature(Main mmu3plugin) {
        this.mmu3plugin = mmu3plugin;
    }

    public Main getPlugin() {
        return mmu3plugin;
    }
}
