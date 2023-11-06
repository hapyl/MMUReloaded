package me.hapyl.mmu3.outcast.game.games;

import com.google.common.collect.Lists;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.PersistentPlayerData;
import me.hapyl.mmu3.outcast.game.Arguments;
import me.hapyl.mmu3.outcast.game.Game;
import me.hapyl.mmu3.outcast.game.GameInstance;
import me.hapyl.mmu3.utils.PanelGUI;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.math.Numbers;
import me.hapyl.spigotutils.module.player.PlayerLib;
import me.hapyl.spigotutils.module.util.ThreadRandom;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import java.util.List;

public class Ultrasequencer extends Game {

    private final int[] allowedSlots;
    private final Material[] dyeGradient = {
            Material.WHITE_DYE,
            Material.ORANGE_DYE,
            Material.MAGENTA_DYE,
            Material.LIGHT_BLUE_DYE,
            Material.YELLOW_DYE,
            Material.LIME_DYE,
            Material.PINK_DYE,
            Material.GRAY_DYE,
            Material.LIGHT_GRAY_DYE,
            Material.CYAN_DYE,
            Material.PURPLE_DYE,
            Material.BLUE_DYE,
            Material.BROWN_DYE,
            Material.GREEN_DYE,
            Material.RED_DYE,
            Material.BLACK_DYE
    };

    public Ultrasequencer() {
        super("Ultrasequencer", PanelGUI.Size.FOUR);

        allowedSlots = new int[27];

        for (int slot = 9, i = 0; slot <= 35; slot++, i++) {
            allowedSlots[i] = slot;
        }
    }

    @Nonnull
    @Override
    protected GameInstance newInstance(Player player, @Nonnull Arguments arguments) {
        return new GameInstance(player, this) {

            private State state = State.PREPARE;
            private BukkitTask task;
            private int round = 0;
            private final List<Integer> currentRound = Lists.newArrayList();
            private int playerClick;

            @Override
            public void onGameStart() {
                gui.fillLine(0, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("&f").build());
                nextRound();
            }

            @Override
            public boolean onGameStop() {
                PersistentPlayerData.getData(getPlayer()).setUltrasequencerRound(round);
                cancelTask();

                return true;
            }

            @Override
            public void onClick(int slot, ClickType clickType) {
                if (state != State.GAME) {
                    return;
                }

                final int nextClick = currentRound.get(playerClick);
                if (nextClick != slot) {
                    endGame();
                    return;
                }

                revealItem(slot);
                playerClick++;

                // Round Complete
                if (playerClick >= currentRound.size()) {
                    PlayerLib.playSound(getPlayer(), Sound.ENTITY_PLAYER_LEVELUP, 2.0f);
                    nextRound();
                    return;
                }

                // Fx
                PlayerLib.playSound(getPlayer(), Sound.BLOCK_LEVER_CLICK, 2.0f);
            }

            public void nextRound() {
                state = State.PREPARE;
                round++;
                currentRound.clear();
                playerClick = 0;

                if (task != null) {
                    task.cancel();
                }

                // Empty board
                clearBoard();

                // Create a round and show menu
                for (int i = 0; i < round; i++) {
                    final int slot = randomSlot();
                    final int iPlusOne = i + 1;

                    currentRound.add(slot);
                    setItem(slot, new ItemBuilder(getColor(i)).setName("&a" + iPlusOne).setAmount(iPlusOne).build());
                }

                setItem(40, new ItemBuilder(Material.ENDER_EYE).setName("&aRemember the order!").build());

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        clearBoard();
                        state = State.GAME;
                        startRoundTask();
                    }
                }.runTaskLater(Main.getPlugin(), 30L + (round * 10L));

            }

            private void cancelTask() {
                if (task != null) {
                    task.cancel();
                }

                task = null;
            }

            private void revealItem(int slot) {
                setItem(slot, new ItemBuilder(getColor(playerClick)).setName("&a" + (playerClick + 1)).build());
            }

            private void startRoundTask() {
                task = new BukkitRunnable() {
                    private int roundSec = 3 + round;

                    @Override
                    public void run() {
                        if (roundSec-- < 0 || state == State.END) {
                            endGame();
                            cancelTask();
                            return;
                        }

                        // Update timer
                        setItem(
                                40,
                                new ItemBuilder(Material.CLOCK)
                                        .setName("&aRound Time")
                                        .setSmartLore("This is how much time is left for you to guess!")
                                        .setAmount(Numbers.clamp(roundSec, 1, 64))
                                        .build()
                        );

                    }
                }.runTaskTimer(Main.getPlugin(), 0L, 20L);
            }

            private void endGame() {
                state = State.END;
                cancelTask();

                clearBoard();
                gui.fillItem(9, 35, new ItemBuilder(Material.LILAC)
                        .setName("&aGame Ended!")
                        .addLore()
                        .addLore("&7Round Reached: &e" + round)
                        .addLore()
                        .addLore("&eClick to flex!")
                        .build(), player -> {
                    Chat.broadcast(
                            "&d&l%s! &a%s &7reached round &b%s&7!",
                            getGame().getName().toUpperCase(),
                            player.getName(),
                            round
                    );
                    player.closeInventory();
                });

                PlayerLib.playSound(getPlayer(), Sound.ENTITY_VILLAGER_YES, 1.0f);
            }

            private void clearBoard() {
                gui.fillItem(9, 35, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("&7").build());
            }

            public int randomSlot() {
                final int random = allowedSlots[ThreadRandom.nextInt(allowedSlots.length)];
                return currentRound.contains(random) ? randomSlot() : random;
            }

            private Material getColor(int i) {
                if (i >= dyeGradient.length) {
                    return getColor(i - dyeGradient.length);
                }

                return dyeGradient[i];
            }
        };
    }

    private enum State {
        PREPARE,
        GAME,
        END
    }

}
