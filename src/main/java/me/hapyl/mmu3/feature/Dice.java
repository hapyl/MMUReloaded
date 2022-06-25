package me.hapyl.mmu3.feature;

import me.hapyl.mmu3.Main;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.entity.Entities;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.player.PlayerLib;
import me.hapyl.spigotutils.module.util.BukkitUtils;
import me.hapyl.spigotutils.module.util.Nulls;
import me.hapyl.spigotutils.module.util.Runnables;
import me.hapyl.spigotutils.module.util.ThreadRandom;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class Dice {

    private static final ItemStack[] diceTextures = new ItemStack[] {
            createHead("6e22c298e7c6336af17909ac1f1ee6834b58b1a3cc99aba255ca7eaeb476173"),
            createHead("71b7a73fc934c9de9160c0fd59df6e42efd5d0378e342b68612cfec3e894834a"),
            createHead("abe677a1e163a9f9e0afcfcde0c95365553478f99ab11152a4d97cf85dbe66f"),
            createHead("af2996efc2bb054f53fb0bd106ebae675936efe1fef441f653c2dce349738e"),
            createHead("e0d2a3ce4999fed330d3a5d0a9e218e37f4f57719808657396d832239e12"),
            createHead("41a2c088637fee9ae3a36dd496e876e657f509de55972dd17c18767eae1f3e9") };

    private final Player player;
    private final ArmorStand stand;
    private final int number;

    public Dice(Player player) {
        this.player = player;
        this.number = pickRandomNumber();
        this.stand = Entities.ARMOR_STAND.spawn(player.getLocation(), self -> {
            self.setInvisible(true);
            self.setGravity(false);
            self.setInvulnerable(true);
            BukkitUtils.lockArmorStand(self);
            Nulls.runIfNotNull(self.getEquipment(), eq -> eq.setHelmet(diceTextures[number]));
        });
        playAnimation();
    }

    public int pickRandomNumber() {
        return ThreadRandom.nextInt(diceTextures.length);
    }

    private void showRolledNumber() {
        stand.setHeadPose(EulerAngle.ZERO);
        stand.setCustomName(Chat.format("%s&l%s", number < 3 ? "&c" : number == 5 ? "&d" : "&a", number + 1));
        stand.setCustomNameVisible(true);

        final Location location = stand.getLocation();
        if (number < 5) {
            PlayerLib.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 0.4f * (number + 1.0f));
        }
        else {
            PlayerLib.playSound(location, Sound.ENTITY_PLAYER_LEVELUP, 0.75f);
            PlayerLib.playSound(location, Sound.ENTITY_PLAYER_LEVELUP, 1.0f);
            PlayerLib.playSound(location, Sound.ENTITY_PLAYER_LEVELUP, 1.25f);
        }

        Runnables.runLater(() -> {
            PlayerLib.spawnParticle(location.add(0.0d, 2.0d, 0.0d), Particle.CLOUD, 5, 0.25, 0.25, 0.25, 0.05f);
            stand.remove();
        }, 20L);
    }

    public void playAnimation() {
        final Location location = player.getLocation();
        final Vector vector = location.getDirection().normalize().setY(0.0d).multiply(0.175d);
        new BukkitRunnable() {
            private final int maxTick = 20;
            private int tick = 0;

            @Override
            public void run() {
                if (tick > maxTick) {
                    this.cancel();
                    showRolledNumber();
                    return;
                }

                // Rolling animation (kinda)
                if (tick % 3 == 0) {
                    stand.setHeadPose(new EulerAngle(
                            ThreadRandom.nextDouble(180),
                            ThreadRandom.nextDouble(60),
                            ThreadRandom.nextDouble(60)
                    ));

                    PlayerLib.playSound(stand.getLocation(), Sound.BLOCK_WOOD_STEP, 0.0f);
                }

                final Location nextLocation = location.add(vector);

                if (tick <= (maxTick / 2)) {
                    nextLocation.add(0.0d, 0.065d, 0.0d);
                }
                else {
                    nextLocation.subtract(0.0d, 0.175d, 0.0d);
                }

                stand.teleport(nextLocation);
                ++tick;
            }

        }.runTaskTimer(Main.getInstance(), 0L, 1L);
    }

    private static ItemStack createHead(String url) {
        return ItemBuilder.playerHeadUrl(url).build();
    }

}
