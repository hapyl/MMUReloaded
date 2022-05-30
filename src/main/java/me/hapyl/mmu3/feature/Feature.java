package me.hapyl.mmu3.feature;

import me.hapyl.mmu3.Main;
import org.bukkit.event.Listener;

public class Feature {

    private final Main mmu3plugin;

    public Feature(Main mmu3plugin) {
        this.mmu3plugin = mmu3plugin;
        if (isListener()) {
            mmu3plugin.getServer().getPluginManager().registerEvents((Listener) this, mmu3plugin);
        }
    }

    public final boolean isListener() {
        return this instanceof Listener;
    }

    public final Main getPlugin() {
        return mmu3plugin;
    }
}
