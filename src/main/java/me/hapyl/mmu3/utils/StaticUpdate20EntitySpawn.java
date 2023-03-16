package me.hapyl.mmu3.utils;

import me.hapyl.spigotutils.module.reflect.Reflect;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.level.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import javax.annotation.Nullable;

public final class StaticUpdate20EntitySpawn {

    @Nullable
    public static Entity spawn(Location location, EntityType20 type) {
        if (type == null) {
            return null;
        }

        if (location.getWorld() == null) {
            throw new IllegalArgumentException("world must be loaded");
        }

        final EntityTypes<?> nmsRef = type.getNmsRef();
        final World world = Reflect.getMinecraftWorld(location.getWorld());

        //final net.minecraft.world.entity.Entity entity = nmsRef.spawn(
        //        (WorldServer) Reflect.getMinecraftWorld(location.getWorld()),
        //        new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()),
        //        EnumMobSpawn.n,
        //        CreatureSpawnEvent.SpawnReason.CUSTOM
        //);


        final net.minecraft.world.entity.Entity entity = new Sniffer(EntityTypes.aN, world);

        entity.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        world.getWorld().addEntity(entity, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.CUSTOM);

        System.out.println(entity);

        if (entity == null) {
            Bukkit.getLogger().warning("Unable to spawn NMS entity of " + type);
            return null;
        }

        return entity.getBukkitEntity();
    }

    public enum EntityType20 {
        SNIFFER(EntityTypes.aN, EntityType.SNIFFER),
        ;

        private final EntityTypes<?> nmsRef;
        private final EntityType type;

        EntityType20(EntityTypes<Sniffer> nmsRef, EntityType type) {
            this.nmsRef = nmsRef;
            this.type = type;
        }

        public static EntityType20 fromEntity(EntityType entity) {
            for (EntityType20 value : values()) {
                if (value.type == entity) {
                    return value;
                }
            }

            return null;
        }

        public EntityTypes<?> getNmsRef() {
            return nmsRef;
        }
    }

}
