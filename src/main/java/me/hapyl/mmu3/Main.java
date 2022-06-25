package me.hapyl.mmu3;

import me.hapyl.mmu3.feature.Calculate;
import me.hapyl.mmu3.feature.itemcreator.ItemCreatorFeature;
import me.hapyl.mmu3.feature.lastlocation.LastLocation;
import me.hapyl.mmu3.feature.lastlocation.LastLocationListener;
import me.hapyl.mmu3.feature.specialblocks.SpecialBlockListener;
import me.hapyl.mmu3.feature.specialblocks.SpecialBlocks;
import me.hapyl.mmu3.feature.standeditor.StandEditor;
import me.hapyl.mmu3.feature.standeditor.StandEditorListener;
import me.hapyl.mmu3.feature.statechanger.StateChanger;
import me.hapyl.mmu3.feature.statechanger.StateChangerListener;
import me.hapyl.mmu3.listener.EntityRemovalListener;
import me.hapyl.mmu3.listener.PlayerListener;
import me.hapyl.mmu3.test.Test;
import me.hapyl.spigotutils.EternaAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;
    private final PluginManager pluginManager = getServer().getPluginManager();

    private FeatureRegistry registry;
    private EternaAPI eternaAPI;

    @Override
    public void onEnable() {
        if (instance != null) {
            throw new IllegalStateException("Instance already initiated!");
        }

        instance = this;

        if (pluginManager.getPlugin("EternaAPI") == null) {
            Bukkit.getLogger().severe("This plugins depends on EternaAPI! Please put it in you plugins folder.");
            pluginManager.disablePlugin(this);
            return;
        }

        // Initiate API
        eternaAPI = new EternaAPI(this);

        // Register Commands
        new CommandRegistry(this).registerCommands();

        // Register events
        pluginManager.registerEvents(new PlayerListener(), this);
        pluginManager.registerEvents(new EntityRemovalListener(), this);

        // Register features events
        new StandEditorListener(this);
        new StateChangerListener(this);
        new SpecialBlockListener(this);
        new LastLocationListener(this);

        // Initiate features
        registry = new FeatureRegistry(this);

        // Reload PersistentPlayerData
        for (Player player : Bukkit.getOnlinePlayers()) {
            PersistentPlayerData.createData(player);
        }

        // Test
        new Test();
    }

    // Features getter
    // * Made these static for easier access
    public static StateChanger getStateChanger() {
        return instance.registry.stateChanger;
    }

    public static StandEditor getStandEditor() {
        return instance.registry.standEditor;
    }

    public static SpecialBlocks getSpecialBlocks() {
        return instance.registry.specialBlocks;
    }

    public static Calculate getCalculate() {
        return instance.registry.calculate;
    }

    public static LastLocation getLastLocation() {
        return instance.registry.lastLocation;
    }

    public static ItemCreatorFeature getItemCreator() {
        return instance.registry.itemCreator;
    }

    // End of feature static members

    public static FeatureRegistry getRegistry() {
        return instance.registry;
    }

    @Override
    public void onDisable() {
        runSafe(() -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PersistentPlayerData.getData(player).saveData();
            }
        }, "persistent player data save");
    }

    private void runSafe(Runnable runnable, String name) {
        try {
            runnable.run();
        } catch (Exception e) {
            Bukkit.getLogger().severe("Could not run %s! %s".formatted(name, e.getMessage()));
        }
    }

    private void registerEvent(Listener listener) {
        pluginManager.registerEvents(listener, this);
    }

    public EternaAPI getEternaAPI() {
        return eternaAPI;
    }

    public static Main getMain() {
        return getInstance();
    }

    public static Main getPlugin() {
        return getInstance();
    }

    public static Main getInstance() {
        return instance;
    }


}
