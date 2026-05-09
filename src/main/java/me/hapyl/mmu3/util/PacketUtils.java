package me.hapyl.mmu3.util;

import net.minecraft.network.protocol.Packet;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class PacketUtils {
    
    private PacketUtils() {
    }
    
    public static void sendPacket(@NotNull Player player, @NotNull Packet<?> packet) {
        ((CraftPlayer) player).getHandle().connection.send(packet);
    }
    
}
