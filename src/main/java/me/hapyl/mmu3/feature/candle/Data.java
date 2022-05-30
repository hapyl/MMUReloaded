package me.hapyl.mmu3.feature.candle;

import org.bukkit.entity.Player;

public class Data {

    private final Player player;
    private Candle candle;
    private boolean offset;

    public Data(Player player) {
        this.player = player;
        this.candle = Candle.WHITE;
        this.offset = false;
    }

    public Player getPlayer() {
        return player;
    }

    public Candle getCandle() {
        return candle;
    }

    public void setCandle(Candle candle) {
        this.candle = candle;
    }

    public boolean isOffset() {
        return offset;
    }

    public void setOffset(boolean offset) {
        this.offset = offset;
    }
}
