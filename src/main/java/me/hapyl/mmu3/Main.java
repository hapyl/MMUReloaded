package me.hapyl.mmu3;

import me.hapyl.eterna.module.entity.Entities;
import me.hapyl.mmu3.command.worldedit.WorldEditShortcutRegistry;
import me.hapyl.mmu3.feature.Calculate;
import me.hapyl.mmu3.feature.itemcreator.ItemCreatorFeature;
import me.hapyl.mmu3.feature.lastlocation.LastLocation;
import me.hapyl.mmu3.feature.lastlocation.LastLocationListener;
import me.hapyl.mmu3.feature.specialblocks.SpecialBlocks;
import me.hapyl.mmu3.feature.standeditor.StandEditor;
import me.hapyl.mmu3.feature.standeditor.StandEditorListener;
import me.hapyl.mmu3.feature.statechanger.StateChanger;
import me.hapyl.mmu3.feature.statechanger.StateChangerListener;
import me.hapyl.mmu3.listener.EntityListener;
import me.hapyl.mmu3.listener.EntityRemovalListener;
import me.hapyl.mmu3.listener.PlayerListener;
import me.hapyl.eterna.EternaAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;
    private final PluginManager pluginManager = getServer().getPluginManager();

    private FeatureRegistry registry;
    private EternaAPI eternaAPI;
    private Entities.EntityCache entityCache;

    public boolean hasWorldEdit;

    @Override
    public void onEnable() {
        instance = this;

        // Initiate API
        eternaAPI = new EternaAPI(this, "4.13.1");
        entityCache = new Entities.EntityCache();

        // Register Commands
        new CommandRegistry(this);

        // Check for world edit
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (plugin.getName().contains("WorldEdit")) {
                hasWorldEdit = true;
                break;
            }
        }

        // Register World Edit shortcuts
        if (hasWorldEdit) {
            new WorldEditShortcutRegistry(this);
        }

        // Register events
        pluginManager.registerEvents(new PlayerListener(), this);
        pluginManager.registerEvents(new EntityRemovalListener(), this);

        // Register features events
        new StandEditorListener(this);
        new StateChangerListener(this);
        new LastLocationListener(this);
        new EntityListener(this);

        // Initiate features
        registry = new FeatureRegistry(this);

        // Reload PersistentPlayerData
        for (Player player : Bukkit.getOnlinePlayers()) {
            PersistentPlayerData.createData(player);
        }

        // Load warp config
        registry.warps.getConfig().loadWarps();
    }

    // Feature getter
    // * Made these statics for easier access
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

    public static Entities.EntityCache entityCache() {
        return instance.entityCache;
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

        runSafe(() -> {
            registry.warps.getConfig().saveWarps();
        }, "warp save");
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
