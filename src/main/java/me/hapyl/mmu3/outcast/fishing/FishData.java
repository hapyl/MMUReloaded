package me.hapyl.mmu3.outcast.fishing;

import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents fishing data of a reel such as bait used, how good reeling was and other factors.
 */
public class FishData {

    private final Player player;
    private final Bait bait;
    private final RodSize rodSize;
    private final float randomLuck;

    private int overrideSize;
    private FishType fish;

    public FishData(Player player) {
        this(player, null, RodSize.SMALL);
    }

    public FishData(Player player, Bait bait, RodSize size) {
        this.player = player;
        this.bait = bait;
        this.randomLuck = ThreadLocalRandom.current().nextFloat();
        this.overrideSize = 0;
        this.rodSize = size;
    }

    public RodSize getRodSize() {
        return rodSize;
    }

    @Nullable
    public FishType getFish() {
        return fish;
    }

    public void setFish(FishType fish) {
        this.fish = fish;
    }

    public int getOverrideSize() {
        return overrideSize;
    }

    public void setOverrideSize(int overrideSize) {
        this.overrideSize = overrideSize;
    }

    public Player getPlayer() {
        return player;
    }

    public Bait getBait() {
        return bait;
    }

    public float getRandomLuck() {
        return randomLuck;
    }

    public int getRandomSize(Fish fish) {
        if (overrideSize > 0) {
            return overrideSize;
        }
        else {
            final FishProperties properties = fish.getProperties();
            final int minSize = properties.getMinSize();
            final int maxSize = properties.getMaxSize();
            final int randomSize = ThreadLocalRandom.current().nextInt(minSize, maxSize);

            return (int) Math.min(randomSize, randomSize + (((maxSize - minSize) / (4 - randomLuck)) * randomLuck));
        }
    }
}
