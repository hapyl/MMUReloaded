package me.hapyl.mmu3.feature;

import me.hapyl.mmu3.Main;
import org.bukkit.event.Listener;

public class QuickUndo extends Feature implements Listener {

    public QuickUndo(Main mmu3plugin) {
        super(mmu3plugin);
        setDescription("Allows creative player to quick undo world edit/sniper action by pressing the button.");
    }
}
