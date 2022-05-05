package me.hapyl.mmu3;

import kz.hapyl.spigotutils.EternaAPI;
import kz.hapyl.spigotutils.module.command.CommandProcessor;
import me.hapyl.mmu3.command.StateChangerCommand;
import me.hapyl.mmu3.feature.statechanger.StateChanger;
import me.hapyl.mmu3.feature.statechanger.StateChangerListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    private final PluginManager pluginManager = getServer().getPluginManager();
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

        // Register Events
        registerEvent(new StateChangerListener());

        // Register Commands
        final CommandProcessor processor = new CommandProcessor(this);
        processor.registerCommand(new StateChangerCommand("statechagner"));

        // Initiate API
        eternaAPI = new EternaAPI(this);

        // Initiate features
        stateChanger = new StateChanger(this);

    }

    // Features
    private StateChanger stateChanger;

    // Features getter
    public StateChanger getStateChanger() {
        return stateChanger;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerEvent(Listener listener) {
        pluginManager.registerEvents(listener, this);
    }

    public EternaAPI getEternaAPI() {
        return eternaAPI;
    }

    public static Main getInstance() {
        return instance;
    }


}
