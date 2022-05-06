package me.hapyl.mmu3.utils;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.standeditor.StandEditor;
import me.hapyl.mmu3.feature.statechanger.StateChanger;
import org.bukkit.event.Listener;

public class InjectListener implements Listener {

    private final Main main;

    public InjectListener(Main main) {
        this.main = main;
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    private Main getMain() {
        return main;
    }

    public StandEditor standEditor() {
        return main.getStandEditor();
    }

    public StateChanger stateChanger() {
        return main.getStateChanger();
    }

}
