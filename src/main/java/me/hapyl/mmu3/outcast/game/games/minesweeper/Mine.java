package me.hapyl.mmu3.outcast.game.games.minesweeper;

public class Mine {

    private final int x;
    private final int z;
    private final int slot;
    private int nearbyMines;
    private boolean isRevealed;
    private boolean isBomb;
    private boolean isMarked;

    public Mine(int index, int x, int z) {
        this.slot = index;
        this.x = x;
        this.z = z;
        this.nearbyMines = 0;
        this.isBomb = false;
        this.isMarked = false;
        this.isRevealed = false;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void reveal() {
        if (isRevealed) {
            return;
        }
        isRevealed = true;
    }

    public int getSlot() {
        return slot;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public void mark() {
        isMarked = !isMarked;
    }

    public void setBomb(boolean bomb) {
        isBomb = bomb;
        nearbyMines = 0;
    }

    public boolean isBomb() {
        return isBomb;
    }

    public void incrementNearbyMines() {
        this.nearbyMines += 1;
    }

    public int getNearbyMines() {
        return nearbyMines;
    }

    @Override
    public String toString() {
        return "" + this.slot + (isBomb ? "*" : "");
    }
}
