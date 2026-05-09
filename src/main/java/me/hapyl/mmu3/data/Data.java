package me.hapyl.mmu3.data;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class Data {
    
    private final Player player;
    
    public Data(@NotNull Player player) {
        this.player = player;
    }
    
    @NotNull
    public Player getPlayer() {
        return player;
    }
    
    public abstract void reset();
    
}
