package me.hapyl.mmu3.outcast.game.games.benchmark;

public class RoundData {

    private final int round;
    private final long reactTime;
    private final long reactTimePingRelative;

    public RoundData(int round, long reactTime, int clickPing) {
        this.round = round;
        this.reactTime = reactTime;
        this.reactTimePingRelative = reactTime - clickPing;
    }

    public int getRound() {
        return round;
    }

    public long getReactTime() {
        return reactTime;
    }

    public long getReactTimePingRelative() {
        return reactTimePingRelative;
    }
}
