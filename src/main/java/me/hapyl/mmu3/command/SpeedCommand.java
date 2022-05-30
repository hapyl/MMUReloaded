package me.hapyl.mmu3.command;

import me.hapyl.mmu3.Message;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.math.Numbers;
import me.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SpeedCommand extends SimplePlayerAdminCommand {

    public SpeedCommand(String name) {
        super(name);
        setDescription("Changes players flying or walking speed.");
        setUsage("speed (number/reset) OR speed (walking/flying) (number/reset) [player]");
        addCompleterValues(1, "walking", "flying", "reset");
    }

    @Override
    protected void execute(Player player, String[] args) {
        // speed 2
        // speed reset

        // speed walk 2
        // speed walk reset

        // speed walk 2 hapyl
        if (args.length == 0) {
            final Type speedType = getSpeedType(player);
            final float speed = getSpeed(player, speedType);
            Message.info(player, "Your %s speed is %s.", speedType.name().toLowerCase(), speed);
            return;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reset")) {
                resetSpeed(player, getSpeedType(player));
            }
            else {
                setSpeed(player, Validate.getFloat(args[0]));
            }
            return;
        }

        final Type type = Validate.getEnumValue(Type.class, args[0]);

        if (args[1].equalsIgnoreCase("reset")) {
            resetSpeed(player, type);
            return;
        }

        float speed = Validate.getFloat(args[1]);
        final Player target = args.length >= 3 ? Bukkit.getPlayer(args[2]) : player;

        if (type == null) {
            Message.error(player, "Invalid speed type! Use one of these: " + Arrays.toString(Type.values()));
            return;
        }

        if (speed < 0.0f || speed > 1.0f) {
            Message.error(player, "Speed cannot be negative nor greater than 1.0! (%s)", speed);
            return;
        }

        if (target == null) {
            Message.error(player, "This player is not online!");
            return;
        }

        setSpeed(target, type, speed);
        if (player != target) {
            Message.info(player, "Changed %s's %s speed to %s.", target.getName(), type.getName(), speed);
        }

    }

    public void setSpeed(Player player, float speed) {
        setSpeed(player, getSpeedType(player), speed);
    }

    public Type getSpeedType(Player player) {
        return player.isFlying() ? Type.FLYING : Type.WALKING;
    }

    public void setSpeed(Player player, Type type, float speed) {
        speed = Numbers.clamp(speed, 0.0f, 1.0f);
        if (type == Type.FLYING) {
            player.setFlySpeed(speed);
        }
        else {
            player.setWalkSpeed(speed);
        }
        Message.success(player, "Set your %s speed to %s.", type.getName(), speed);
    }

    public void resetSpeed(Player player, Type type) {
        if (type == Type.WALKING) {
            player.setWalkSpeed(type.getSpeed());
        }
        else {
            player.setFlySpeed(type.getSpeed());
        }
        Message.success(player, "Reset your %s speed.", type.getName());
    }

    public float getSpeed(Player player, Type type) {
        if (type == Type.WALKING) {
            return player.getWalkSpeed();
        }
        return player.getFlySpeed();
    }


    private enum Type {
        WALKING(0.2f),
        FLYING(0.1f);

        private final float speed;

        Type(float speed) {
            this.speed = speed;
        }

        public float getSpeed() {
            return speed;
        }

        public String getName() {
            return name().toLowerCase();
        }

    }

}
