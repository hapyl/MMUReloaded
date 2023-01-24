package me.hapyl.mmu3.outcast.fishing;

import me.hapyl.spigotutils.module.math.Numbers;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ReelTask extends BukkitRunnable {

    private final Player player;
    private final ReelUI ui;
    private final FishData data;

    // Reel info
    protected float progress = 0.0f;
    protected float fishPos = 0;   // pos is between MAX_POS
    protected float playerPos = 0; // pos is between MAX_POS

    private final int MAX_POS = 19;

    public ReelTask(ReelUI ui, FishData data) {
        this.ui = ui;
        this.player = data.getPlayer();
        this.data = data;
    }

    protected void increasePlayerPos(float increment) {
        playerPos = Numbers.clamp(playerPos + increment, 0, MAX_POS);
    }

    @Override
    public void run() {
        if (progress >= 100) {
            this.cancel();
            return;
        }

        playerPos = Numbers.clamp(playerPos - 0.5f, 0, MAX_POS);

        // Update inventory
        ui.updateInventory();
    }
}
