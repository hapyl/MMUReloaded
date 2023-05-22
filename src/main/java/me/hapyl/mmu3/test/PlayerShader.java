package me.hapyl.mmu3.test;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;

public class PlayerShader {

    public static void set(Player player, ShaderType type) {
        final PacketContainer packet = new PacketContainer(PacketType.Play.Server.CAMERA);
        packet.getIntegers().write(0, type.id);

        System.out.println(packet.toString());

        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
    }

    public enum ShaderType {
        CREEPER(50),
        SPIDER(52),
        ENDERMAN(58);

        public final int id;

        ShaderType(int id) {
            this.id = id;
        }
    }

}
