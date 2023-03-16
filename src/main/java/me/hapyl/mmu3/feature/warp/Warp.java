package me.hapyl.mmu3.feature.warp;

import me.hapyl.mmu3.message.Message;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Warp {

    private final String name;
    private final Location location;

    private String creator;

    public Warp(String name, Location location) {
        this.name = name;
        this.location = location;
        this.creator = "System";
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public void teleport(Entity entity) {
        entity.teleport(location);
        if (entity instanceof Player player) {
            Message.info(player, "Teleported to warp \"%s\".", name);
            Message.sound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f);
        }
    }

    public void setLocation(Location location) {
        this.location.setX(location.getX());
        this.location.setY(location.getY());
        this.location.setZ(location.getZ());
        this.location.setYaw(location.getYaw());
        this.location.setPitch(location.getPitch());
    }
}
