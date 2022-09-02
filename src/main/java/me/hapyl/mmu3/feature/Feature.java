package me.hapyl.mmu3.feature;

import me.hapyl.mmu3.Main;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

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

    protected void runTaskLater(Runnable runnable, long delay) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskLater(mmu3plugin, delay);
    }

    public final Main getPlugin() {
        return mmu3plugin;
    }
}
