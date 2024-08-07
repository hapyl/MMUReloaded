package me.hapyl.mmu3.feature;

import me.hapyl.eterna.module.reflect.Reflect;
import net.minecraft.network.protocol.game.PacketPlayOutGameStateChange;
import org.bukkit.entity.Player;

public enum PlayerPacket {

    START_RAINING(PacketPlayOutGameStateChange.c, 0.0F),
    END_RAINING(PacketPlayOutGameStateChange.b, 0.0F),
    SHOW_CREDITS(PacketPlayOutGameStateChange.e, 1.0F),
    SHOW_WELCOME_SCREEN(PacketPlayOutGameStateChange.f, 0.0F),
    TELL_HOW_TO_MOVE(PacketPlayOutGameStateChange.f, 101.0F),
    TELL_HOW_TO_JUMP(PacketPlayOutGameStateChange.f, 102.0F),
    TELL_HOW_TO_OPEN_INVENTORY(PacketPlayOutGameStateChange.f, 103.0F),
    TELL_DEMO_IS_OVER(PacketPlayOutGameStateChange.f, 104.0F),
    SHOW_RESPAWN_SCREEN(PacketPlayOutGameStateChange.l, 0.0F);

    private final PacketPlayOutGameStateChange.a action;
    private final float value;

    PlayerPacket(PacketPlayOutGameStateChange.a action, float value) {
        this.action = action;
        this.value = value;
    }

    public void sendPacket(Player player) {
        // jesus fuck why you need ProtocolLib you're using the other method
        Reflect.sendPacket(player, new PacketPlayOutGameStateChange(action, value));
    }
}
