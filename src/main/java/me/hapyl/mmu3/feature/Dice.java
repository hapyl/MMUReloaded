package me.hapyl.mmu3.feature;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.entity.Entities;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.locaiton.LocationHelper;
import me.hapyl.eterna.module.player.PlayerLib;
import me.hapyl.eterna.module.util.BukkitUtils;
import me.hapyl.eterna.module.util.Runnables;
import me.hapyl.mmu3.Main;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;

public class Dice {

    private static final ItemStack[][] DICE_TEXTURES = new ItemStack[][] {
            new ItemStack[] {
                    createHead("6e22c298e7c6336af17909ac1f1ee6834b58b1a3cc99aba255ca7eaeb476173"),
                    createHead("4675996c9164cf409f9fc9024231ca301a4f024e3a306c8f3f4caa062a5576b8")
            },
            new ItemStack[] {
                    createHead("71b7a73fc934c9de9160c0fd59df6e42efd5d0378e342b68612cfec3e894834a"),
                    createHead("6c8c54dc6c2d40625a72e95d8a80f04a8f9fead318f370a97a82ab8872542477")
            },
            new ItemStack[] {
                    createHead("abe677a1e163a9f9e0afcfcde0c95365553478f99ab11152a4d97cf85dbe66f"),
                    createHead("47ed98ce4e10b59767334789e33b112dafba93691eef64273288c2f1fd324e34")
            },
            new ItemStack[] {
                    createHead("af2996efc2bb054f53fb0bd106ebae675936efe1fef441f653c2dce349738e"),
                    createHead("9e6800bc7b0230694b1c0f24bfa9edb22f3478f9e4ee7fc4c4398a8799f3840e")
            },
            new ItemStack[] {
                    createHead("e0d2a3ce4999fed330d3a5d0a9e218e37f4f57719808657396d832239e12"),
                    createHead("826aa157fe7680b3bd21d53c061e4a61c46a96078d641ca6bbfc604e219de19e")
            },
            new ItemStack[] {
                    createHead("586b745566284a05366baff2807d9d8f8344612aabddeb012c47c7252e34e731"),
                    createHead("41a2c088637fee9ae3a36dd496e876e657f509de55972dd17c18767eae1f3e9")
            },
    };

    private static final double maxTick = 20;

    private final Player player;
    private final ArmorStand stand;
    private final int number;

    public Dice(Player player) {
        this.player = player;
        this.number = pickRandomNumber();
        this.stand = Entities.ARMOR_STAND.spawn(
                player.getLocation(), self -> {
                    self.setInvisible(true);
                    self.setGravity(false);
                    self.setInvulnerable(true);
                    self.getEquipment().setHelmet(DICE_TEXTURES[number][BukkitUtils.RANDOM.nextDouble() <= 0.07 ? 1 : 0]);

                    BukkitUtils.lockArmorStand(self);
                }
        );
        playAnimation();
    }

    public int pickRandomNumber() {
        return ThreadLocalRandom.current().nextInt(DICE_TEXTURES.length);
    }

    public void playAnimation() {
        final Location location = player.getLocation();
        final Vector vector = location.getDirection().normalize().setY(0.0d).multiply(0.175d);

        new BukkitRunnable() {
            private int tick = 0;

            @Override
            public void run() {
                if (tick > maxTick) {
                    this.cancel();
                    showRolledNumber();
                    return;
                }

                // Rolling animation (kinda)
                if (tick % 2 == 0) {
                    final ThreadLocalRandom random = ThreadLocalRandom.current();

                    stand.setHeadPose(new EulerAngle(
                            random.nextDouble(50),
                            random.nextDouble(50),
                            random.nextDouble(50)
                    ));

                    PlayerLib.playSound(location, Sound.BLOCK_WOOD_STEP, 0.0f);
                }

                location.add(vector);

                final double y = Math.sin(Math.PI * 1.3d * (tick / maxTick)) * 1.3d;
                LocationHelper.offset(location, 0, y, 0, () -> stand.teleport(location));

                tick++;
            }

        }.runTaskTimer(Main.getInstance(), 0L, 1L);
    }

    private void showRolledNumber() {
        stand.setHeadPose(EulerAngle.ZERO);
        stand.setCustomName(Chat.format("%s&l%s".formatted(number < 3 ? "&c" : number == 5 ? "&d" : "&a", number + 1)));
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

        Runnables.runLater(
                () -> {
                    PlayerLib.spawnParticle(location.add(0.0d, 2.0d, 0.0d), Particle.CLOUD, 5, 0.25, 0.25, 0.25, 0.05f);
                    stand.remove();
                }, 20L
        );
    }

    private static ItemStack createHead(String url) {
        return ItemBuilder.playerHeadUrl(url).build();
    }

}
