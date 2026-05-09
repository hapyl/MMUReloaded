package me.hapyl.mmu3.feature;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class DiceRoll {

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

    private static final Map<Integer, Style> NUMBER_STYLE = Map.of(
            0, Style.style(TextColor.color(0xFF0000)),
            1, Style.style(TextColor.color(0xBFBF00)),
            2, Style.style(TextColor.color(0x80FF00)),
            3, Style.style(TextColor.color(0x40FF00)),
            4, Style.style(TextColor.color(0xF00FF00)),
            5, Style.style(TextColor.color(0xFF4BD5))
    );

    private static final Random RANDOM = new Random();
    private static final double ANIMATION_DURATION = 20;

    private final Player player;
    private final ArmorStand armorStand;
    private final int number;

    public DiceRoll(@NotNull Player player) {
        this.player = player;
        this.number = pickRandomNumber();
        this.armorStand = player.getWorld().spawn(
                player.getLocation(), ArmorStand.class, self -> {
                    self.setInvisible(true);
                    self.setGravity(false);
                    self.setInvulnerable(true);
                    self.getEquipment().setHelmet(DICE_TEXTURES[number][RANDOM.nextDouble() <= 0.07 ? 1 : 0]);
                }
        );

        this.throwDie();
    }

    public void throwDie() {
        final Location location = player.getLocation();
        final Vector vector = location.getDirection().normalize().setY(0).multiply(0.175d);

        new BukkitRunnable() {
            private int tick = 0;

            @Override
            public void run() {
                if (tick > ANIMATION_DURATION) {
                    showRolledNumber();
                    cancel();
                    return;
                }

                // Rolling animation (kinda)
                if (tick % 2 == 0) {
                    final ThreadLocalRandom random = ThreadLocalRandom.current();

                    armorStand.setHeadPose(new EulerAngle(
                            random.nextDouble(50),
                            random.nextDouble(50),
                            random.nextDouble(50)
                    ));

                    player.playSound(player.getLocation(), Sound.BLOCK_WOOD_STEP, 3, 0.0f);
                }

                location.add(vector);

                final double y = Math.sin(Math.PI * 1.3d * (tick / ANIMATION_DURATION)) * 1.3d;

                location.add(0, y, 0);
                armorStand.teleport(location);
                location.subtract(0, y, 0);

                tick++;
            }

        }.runTaskTimer(Main.getInstance(), 0L, 1L);
    }

    private void showRolledNumber() {

        armorStand.setHeadPose(EulerAngle.ZERO);
        armorStand.customName(Component.text(number + 1, NUMBER_STYLE.get(number)));
        armorStand.setCustomNameVisible(true);

        final Location location = armorStand.getLocation();

        if (number < 5) {
            player.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 3, 0.4f * (number + 1));
        }
        else {
            player.playSound(location, Sound.ENTITY_PLAYER_LEVELUP, 3, 0.75f);
            player.playSound(location, Sound.ENTITY_PLAYER_LEVELUP, 3, 1.0f);
            player.playSound(location, Sound.ENTITY_PLAYER_LEVELUP, 3, 1.25f);
        }

        // Remove the armor stand
        new BukkitRunnable() {
            @Override
            public void run() {
                armorStand.remove();

                player.playSound(location, Sound.ENTITY_CHICKEN_EGG, 3, 1.0f);
                player.spawnParticle(Particle.CLOUD, location.add(0, 2, 0), 5, 0.25, 0.25, 0.25, 0.05f);
            }
        }.runTaskLater(Main.getPlugin(), 20);
    }

    @NotNull
    public static ItemStack createHead(@NotNull String url) {
        return ItemBuilder.playerHead(url).build();
    }

    private static int pickRandomNumber() {
        return ThreadLocalRandom.current().nextInt(DICE_TEXTURES.length);
    }

}
