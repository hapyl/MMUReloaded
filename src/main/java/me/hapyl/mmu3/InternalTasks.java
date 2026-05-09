package me.hapyl.mmu3;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public final class InternalTasks {
    
    private static final Main PLUGIN = Main.getPlugin();
    
    private InternalTasks() {
    }
    
    public static void now(@NotNull Runnable runnable) {
        createRunnable(runnable, BukkitRunnable::runTask);
    }
    
    public static void later(@NotNull Runnable runnable, int delay) {
        createRunnable(runnable, (task, plugin) -> task.runTaskLater(plugin, delay));
    }
    
    private static void createRunnable(@NotNull Runnable runnable, @NotNull Scheduler scheduler) {
        final BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                }
                catch (Exception ex) {
                    PLUGIN.getLogger().severe("Failed to run runnable: " + ex.getMessage());
                    throw new RuntimeException(ex);
                }
            }
        };
        
        
        scheduler.schedule(bukkitRunnable, PLUGIN);
    }
    
    interface Scheduler {
        void schedule(@NotNull BukkitRunnable scheduler, @NotNull Plugin plugin);
    }
    
}