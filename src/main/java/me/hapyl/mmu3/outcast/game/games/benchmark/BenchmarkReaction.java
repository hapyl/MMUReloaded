package me.hapyl.mmu3.outcast.game.games.benchmark;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.outcast.game.Arguments;
import me.hapyl.mmu3.outcast.game.Game;
import me.hapyl.mmu3.outcast.game.GameInstance;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.inventory.gui.GUI;
import me.hapyl.spigotutils.module.util.ThreadRandom;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import java.util.Map;

public class BenchmarkReaction extends Game {

    private final ItemStack ICON_PRE_GAME = new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE)
            .setName("&aClick to start!")
            .setSmartLore("When the red box turns green, click as quickly as you can!")
            .addLore()
            .addSmartLore("There will a total of 5 rounds to determine your average reaction time.")
            .build();

    private final ItemStack ICON_WAITING = new ItemBuilder(Material.RED_STAINED_GLASS_PANE)
            .setName("&aWait...")
            .setSmartLore("When the red box turns green, click as quickly as you can!")
            .build();

    private final ItemStack ICON_REACT = new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("&a&lCLICK!").build();

    public BenchmarkReaction() {
        super("HB %s Reaction".formatted(GUI.ARROW_FORWARD));
    }

    @Nonnull
    @Override
    protected GameInstance newInstance(Player player, @Nonnull Arguments arguments) {
        return new GameInstance(player, this) {

            private final Map<Integer, RoundData> roundData = Maps.newHashMap();
            private Status status = Status.PRE_GAME;

            private float averageReaction;
            private float averageReactionPing;

            @Override
            public void onGameStart() {
                fillPlayableArea(ICON_PRE_GAME);
            }

            @Override
            public void onGameStop() {
                task.cancel();
                task = null;
                roundData.clear();
                stopPlaying();
            }

            @Override
            public void onClick(int slot, ClickType clickType) {
                if (status == Status.PRE_GAME) {
                    nextRound();
                    return;
                }

                if (status == Status.FINISHED) {
                    Chat.broadcast(
                            "&d&lREACTION! &a%s's &7average reaction is &b%sms &7(%sms)!",
                            getPlayer().getName(),
                            averageReactionPing,
                            averageReaction
                    );
                    gui.closeInventory();
                    return;
                }

                if (status == Status.REACT) {
                    final long reactTime = System.currentTimeMillis() - roundStart;
                    final RoundData data = new RoundData(roundData.size(), reactTime, getPlayer().getPing());
                    roundData.put(roundData.size(), data);
                    status = Status.PRE_GAME;
                    fillPlayableArea(new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE)
                                             .setName("Round Results")
                                             .addLore("&b&l%s&bms &7(%s)", data.getReactTimePingRelative(), data.getReactTime())
                                             .addLore()
                                             .addLore("&eClick to continue")
                                             .build());
                    Message.sound(getPlayer(), Sound.ENTITY_VILLAGER_YES);
                    return;
                }

                if (status == Status.WAITING) {
                    task.cancel();
                    status = Status.PRE_GAME;
                    fillPlayableArea(new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE)
                                             .setName("&cToo Soon!")
                                             .addLore("&eClick to try again")
                                             .build());
                    Message.sound(getPlayer(), Sound.ENTITY_VILLAGER_NO);
                }
            }

            private long roundStart;
            private BukkitTask task;

            private void nextRound() {
                // Game Over
                if (roundData.size() >= 5) {
                    status = Status.FINISHED;
                    final ItemBuilder builder = new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("Finished!").addLore();

                    float average = 0.0f;
                    float averagePing = 0.0f;

                    for (int round : roundData.keySet()) {
                        final RoundData data = roundData.get(round);
                        average += data.getReactTime();
                        averagePing += data.getReactTimePingRelative();
                        builder.addLore(
                                "&7Round %s: &b&l%s&bms &7(%sms)",
                                round + 1,
                                data.getReactTimePingRelative(),
                                data.getReactTime()
                        );
                    }

                    averageReaction = average / roundData.size();
                    averageReactionPing = averagePing / roundData.size();

                    builder.addLore();
                    builder.addLore("&7Average Reaction Time");
                    builder.addLore("&b&l%s &7(%sms)", averageReactionPing, averageReaction);
                    builder.addLore();
                    builder.addLore("&eClick to announce");

                    fillPlayableArea(builder.build());
                    return;
                }

                // Start round
                if (task != null) {
                    task.cancel();
                    task = null;
                }

                status = Status.WAITING;
                fillPlayableArea(ICON_WAITING);
                Message.sound(getPlayer(), Sound.BLOCK_NOTE_BLOCK_PLING, 2.0f);

                task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        status = Status.REACT;
                        roundStart = System.currentTimeMillis();
                        fillPlayableArea(ICON_REACT);
                        Message.sound(getPlayer(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2.0f);
                    }
                }.runTaskLater(Main.getPlugin(), ThreadRandom.nextInt(40, 120));

            }

            private void fillPlayableArea(ItemStack stack) {
                gui.fillItem(0, 26, stack);
            }

        };
    }

    private enum Status {
        PRE_GAME, // pre game or round result
        WAITING,  // waiting for the green box
        REACT,    // click now
        FINISHED  // all rounds played
    }

}
