package me.hapyl.mmu3.test;

import me.hapyl.mmu3.Main;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.entity.Entities;
import me.hapyl.spigotutils.module.entity.EntityUtils;
import me.hapyl.spigotutils.module.player.EffectType;
import me.hapyl.spigotutils.module.player.PlayerLib;
import me.hapyl.spigotutils.module.reflect.npc.ClickType;
import me.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import me.hapyl.spigotutils.module.reflect.npc.NPCPose;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class SkyWolf {

    private static HumanNPC npc;
    private static Wolf wolf;

    public static void test(Player player) {
        if (npc != null) {
            remove(player);
            Chat.sendMessage(player, "&aRemoved!");
            return;
        }

        final Location location = player.getLocation();
        location.setPitch(45f);

        PlayerLib.addEffect(player, EffectType.INVISIBILITY, 100000, 1);

        npc = new HumanNPC(location, "", player.getName()) {
            @Override
            public void onClick(Player player, HumanNPC npc, ClickType clickType) {
                if (clickType != ClickType.ATTACK) {
                    return;
                }

                SkyWolf.remove(player);
            }
        };
        npc.showAll();

        npc.setPose(NPCPose.CROUCHING);
        npc.setEquipment(player.getEquipment());

        wolf = Entities.WOLF.spawn(location, me -> EntityUtils.setCollision(me, EntityUtils.Collision.DENY, player));

        new BukkitRunnable() {
            private final int maxTick = 5 * 20;
            private int tick = 0;

            @Override
            public void run() {
                if (tick >= maxTick || npc == null) {
                    remove(player);
                    this.cancel();
                    return;
                }

                if (tick % 10 == 0) {
                    npc.swingOffHand();
                }
                else if (tick % 9 == 0) {
                    npc.swingMainHand();
                }

                // sync with player
                wolf.teleport(player.getLocation());

                ++tick;
            }
        }.runTaskTimer(Main.getPlugin(), 0L, 1L);

        Chat.sendMessage(player, "&aSpawned!");
    }

    private static void remove(Player player) {
        PlayerLib.removeEffect(player, PotionEffectType.INVISIBILITY);
        if (npc != null) {
            player.teleport(npc.getLocation());
            npc.remove();
            npc = null;
        }
        if (wolf != null) {
            wolf.remove();
            wolf = null;
        }
    }

}
