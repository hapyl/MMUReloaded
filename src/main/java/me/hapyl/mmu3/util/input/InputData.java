package me.hapyl.mmu3.util.input;

import me.hapyl.eterna.module.util.Disposable;
import org.bukkit.entity.Player;

public class InputData implements Disposable {

    public final Player player;
    public final InputListener listener;

    private final float[] speed;
    private final boolean[] flight;

    InputData(Player player, InputListener listener) {
        this.player = player;
        this.listener = listener;
        this.speed = new float[] { player.getWalkSpeed(), player.getFlySpeed() };
        this.flight = new boolean[] { player.getAllowFlight(), player.isFlying() };

        // Force player to fly to prevent them from actually moving
        player.setWalkSpeed(0.0f);
        player.setFlySpeed(0.0f);

        // Teleport a little hit higher so we're flying
        player.teleport(player.getLocation().add(0, 0.1, 0));

        player.setAllowFlight(true);
        player.setFlying(true);
    }

    @Override
    public void dispose() {
        player.setWalkSpeed(speed[0]);
        player.setFlySpeed(speed[1]);

        player.setAllowFlight(flight[0]);
        player.setFlying(flight[1]);
    }
}
