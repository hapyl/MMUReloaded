package me.hapyl.mmu3.feature.standeditor;

import me.hapyl.mmu3.Main;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

public class Data {

    protected TuneData.Axis axis = TuneData.Axis.X;
    protected double speed;

    private final Player player;
    private final ArmorStand stand;
    private boolean waitForMove;
    private long enterMoveAt;

    public Data(Player player, ArmorStand stand, boolean waitForMove) {
        this.player = player;
        this.stand = stand;
        this.waitForMove = waitForMove;
        this.speed = 0.1;
    }

    public Data(Player player, ArmorStand stand) {
        this(player, stand, false);
    }

    public boolean canLeaveMoveMode() {
        return (System.currentTimeMillis() - enterMoveAt) >= 100;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public TuneData.Axis getAxis() {
        return axis;
    }

    public void setAxis(TuneData.Axis axis) {
        this.axis = axis;
    }

    public Player getPlayer() {
        return player;
    }

    public ArmorStand getStand() {
        return stand;
    }

    public boolean isWaitForMove() {
        return waitForMove;
    }

    public void setWaitForMove(boolean waitForMove) {
        this.waitForMove = waitForMove;
        this.enterMoveAt = System.currentTimeMillis();
    }

    public String getStandName() {
        final String customName = stand.getCustomName();
        return customName == null ? "Armor Stand" : customName;
    }

    public void setTaken(boolean flag) {
        Main.getStandEditor().setTaken(this, flag);
    }

    public boolean isTaken() {
        return Main.getStandEditor().isTaken(stand);
    }

    public void nextAxis() {
        axis = axis == TuneData.Axis.X ? TuneData.Axis.Y : axis == TuneData.Axis.Y ? TuneData.Axis.Z : TuneData.Axis.X;
    }
}
