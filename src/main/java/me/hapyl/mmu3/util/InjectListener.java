package me.hapyl.mmu3.util;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.specialblocks.SpecialBlocks;
import me.hapyl.mmu3.feature.standeditor.StandEditor;
import me.hapyl.mmu3.feature.statechanger.StateChanger;
import org.bukkit.event.Listener;

public class InjectListener implements Listener {

    protected final Main main;

    public InjectListener(Main main) {
        this.main = main;
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    public StandEditor standEditor() {
        return Main.getStandEditor();
    }

    public StateChanger stateChanger() {
        return Main.getStateChanger();
    }

    public SpecialBlocks specialBlocks() {
        return Main.getSpecialBlocks();
    }

}
