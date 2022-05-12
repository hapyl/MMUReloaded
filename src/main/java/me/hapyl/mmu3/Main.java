package me.hapyl.mmu3;

import kz.hapyl.spigotutils.EternaAPI;
import kz.hapyl.spigotutils.module.command.CommandProcessor;
import kz.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import kz.hapyl.spigotutils.module.config.DataField;
import me.hapyl.mmu3.command.*;
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
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

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
        final CommandProcessor processor = new CommandProcessor(this);
        processor.registerCommand(new StateChangerCommand("stateChanger"));
        processor.registerCommand(new EditStandCommand("editStand"));
        processor.registerCommand(new SpecialBlocksCommand("specialBlocks"));
        processor.registerCommand(new CalculateCommand("calculate"));
        processor.registerCommand(new BackCommand("back"));
        processor.registerCommand(new CenterCommand("center"));
        processor.registerCommand(new ConsoleCommand("console"));
        processor.registerCommand(new SayCommand("say"));
        processor.registerCommand(new ItemCreatorCommand("itemCreator"));
        processor.registerCommand(new EntityRemovalCommand("entityRemoval"));
        processor.registerCommand(new GameModeCommand("gm"));
        processor.registerCommand(new OpenInventoryCommand("openInventory"));
        processor.registerCommand(new RollCommand("roll"));
        processor.registerCommand(new PersonalTimeCommandCommand("personalTime"));
        processor.registerCommand(new PersonalWeatherCommand("personalWeather"));
        processor.registerCommand(new ItemCommand("getItem"));
        processor.registerCommand(new GameCommand("game"));
        processor.registerCommand(new SightBlockCommand("sightBlock"));
        processor.registerCommand(new RawCommand("raw"));
        processor.registerCommand(new LockCommand("lock"));

        processor.registerCommand(new SimplePlayerAdminCommand("testdatafields") {
            @Override
            protected void execute(Player player, String[] strings) {
                final PersistentPlayerData data = PersistentPlayerData.getData(player);

                player.showDemoScreen();

                try {
                    for (Field field : data.getClass().getDeclaredFields()) {
                        final DataField annotation = field.getAnnotation(DataField.class);
                        if (annotation == null) {
                            continue;
                        }

                        final Object value = FieldUtils.readField(field, data, true);
                        Message.debug(
                                player,
                                "%s %s: %s",
                                "&7" + field.getName(),
                                "&e(" + field.getType().getSimpleName() + ")",
                                value == null ? "null" : value
                        );
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

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
