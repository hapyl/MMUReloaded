package me.hapyl.mmu3.outcast.game.games.minesweeper;

import com.google.common.collect.Lists;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.outcast.game.Arguments;
import me.hapyl.mmu3.outcast.game.Game;
import me.hapyl.mmu3.outcast.game.GameInstance;
import me.hapyl.mmu3.utils.PanelGUI;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.player.PlayerLib;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

public class MineSweeper extends Game {
    public MineSweeper() {
        super("Minesweeper", PanelGUI.Size.FIVE);
        setInfo("A minesweeper mini game.");
    }

    @Nonnull
    @Override
    protected GameInstance newInstance(Player player, @Nonnull Arguments arguments) {
        return new GameInstance(player, this, arguments.isDebug()) {

            private final int mines = Math.min(arguments.getInt(0, 5), 24);
            private final long startedAt = System.currentTimeMillis();
            private final Mine[][] field = new Mine[5][5];
            private final int[] validSlots = { 2, 3, 4, 5, 6, 11, 12, 13, 14, 15, 20, 21, 22, 23, 24, 29, 30, 31, 32, 33, 38, 39, 40, 41,
                                               42 };

            private final ChatColor[] dangerColor = { ChatColor.GREEN, ChatColor.DARK_GREEN, ChatColor.YELLOW, ChatColor.GOLD, ChatColor.RED,
                                                      ChatColor.DARK_RED };

            private long finishedAt;
            private State state;

            @Override
            public void onGameStart() {
                if (mines != 5) {
                    Message.info(getPlayer(), "Playing with %s mines!", mines);
                }

                state = State.PLAYING;
                generateBombs();
                fillOuter(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("&f").build());
                updateItems();
            }

            private void generateBombs() {
                int slot = 0;
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        field[i][j] = new Mine(validSlots[slot++], i, j);
                    }
                }

                // Get 5 bombs
                final List<Mine> list = Lists.newArrayList();
                for (Mine[] mines : field) {
                    list.addAll(Lists.newArrayList(mines));
                }

                Collections.shuffle(list);

                for (int i = 0; i < mines; i++) {
                    list.get(i).setBomb(true);
                }

                list.clear();


                // Calculate nearby mines
                for (int i = 0; i < field.length; i++) {
                    for (int j = 0; j < field[i].length; j++) {
                        final Mine mine = field[i][j];
                        // Yeah, my iq is not Long.MAX_VALUE to use loops
                        // for checking the values...
                        if (checkMine(i + 1, j)) {
                            mine.incrementNearbyMines();
                        }
                        if (checkMine(i - 1, j)) {
                            mine.incrementNearbyMines();
                        }
                        if (checkMine(i, j + 1)) {
                            mine.incrementNearbyMines();
                        }
                        if (checkMine(i, j - 1)) {
                            mine.incrementNearbyMines();
                        }
                        if (checkMine(i + 1, j + 1)) {
                            mine.incrementNearbyMines();
                        }
                        if (checkMine(i + 1, j - 1)) {
                            mine.incrementNearbyMines();
                        }
                        if (checkMine(i - 1, j + 1)) {
                            mine.incrementNearbyMines();
                        }
                        if (checkMine(i - 1, j - 1)) {
                            mine.incrementNearbyMines();
                        }
                    }
                }

            }

            private boolean checkMine(int i, int j) {
                if (isOutOfBounds(i, j)) {
                    return false;
                }

                return field[i][j].isBomb();
            }

            private boolean checkZero(int i, int j) {
                if (isOutOfBounds(i, j)) {
                    return false;
                }

                final Mine mine = field[i][j];
                return mine.getNearbyMines() == 0 && !mine.isRevealed();
            }

            private boolean isOutOfBounds(int i, int j) {
                return i < 0 || j < 0 || i >= 5 || j >= 5;
            }

            private void updateItems() {
                for (Mine[] mines : field) {
                    for (final Mine mine : mines) {
                        final int slot = mine.getSlot();

                        if (!mine.isRevealed()) {
                            setItem(
                                    slot,
                                    ItemBuilder
                                            .of(mine.isMarked() ? Material.FILLED_MAP : Material.MAP, "&a???")
                                            .addLore("&eClick to reveal.")
                                            .addLore("&6Right Click to mark.")
                                            .addLoreIf("&4&lDEBUG &f" + (mine.isBomb() ? "&cMine" : "&a" + mine.getNearbyMines()), isDebug())
                                            .build()
                            );
                            continue;
                        }

                        if (mine.isBomb()) {
                            setItem(slot, new ItemBuilder(Material.TNT).setName("&c&lBOOM!").build());
                            continue;
                        }

                        final int nearbyMines = mine.getNearbyMines();
                        final ChatColor color = dangerColor[Math.min(nearbyMines, 5)];

                        if (nearbyMines == 0) {
                            setItem(slot, new ItemBuilder(Material.OAK_BUTTON).setName(color + "There are no mines nearby!").build());
                        }
                        else {
                            setItem(
                                    slot,
                                    new ItemBuilder(Material.STONE_BUTTON)
                                            .setName(color + "There are %s mines nearby!", nearbyMines)
                                            .setAmount(nearbyMines)
                                            .build()
                            );
                        }
                    }
                }
            }

            private void revealAll() {
                fillOuter(ItemBuilder.of(Material.RED_STAINED_GLASS_PANE, "&cYou lost!").build());
                for (Mine[] mines : field) {
                    for (Mine mine : mines) {
                        mine.reveal();
                    }
                }

                updateItems();
            }

            @Override
            public void onClick(int slot, ClickType clickType) {
                final Player player = getPlayer();

                // If clicking when won, display time.
                if (state == State.WON) {
                    final String gameTime = new SimpleDateFormat("ss.SSS").format(finishedAt - startedAt);
                    Chat.broadcast("&a%s &7defused %s mines in &a%s!", player.getName(), mines, gameTime);
                }

                // If clicked when not playing, close inventory.
                if (state != State.PLAYING) {
                    player.closeInventory();
                    return;
                }

                final Mine mine = getMineBySlot(slot);
                if (mine == null) {
                    return;
                }

                if (clickType.isLeftClick() && !mine.isRevealed()) {
                    mine.reveal();

                    // Test for 0
                    if (mine.getNearbyMines() == 0) {
                        checkNeighbourForZero(mine.getX(), mine.getZ());
                    }

                    if (mine.isBomb()) {
                        stopGame(State.LOST);
                        PlayerLib.playSound(player, Sound.ENTITY_GENERIC_EXPLODE, 0.75f);
                        return;
                    }
                }
                else if (clickType.isRightClick()) {
                    mine.mark();
                }

                updateItems();
                PlayerLib.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f);

                // Check for win
                for (Mine[] mines : field) {
                    for (Mine inMine : mines) {
                        if (!inMine.isBomb() && !inMine.isRevealed()) {
                            return;
                        }
                    }
                }

                stopGame(State.WON);
                PlayerLib.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0f);
                fillOuter(ItemBuilder.of(
                        Material.GREEN_STAINED_GLASS_PANE,
                        "&aYou won!",
                        "&7It took you %ss!".formatted(Chat.formatTimeString(finishedAt - startedAt)),
                        "",
                        "&eClick to flex your time!"
                ).build());
            }

            private void checkNeighbourForZero(int i, int j) {
                final Mine mine = field[i][j];
                if (mine.getNearbyMines() != 0) {
                    return;
                }

                mine.reveal();

                if (checkZero(i + 1, j)) {
                    checkNeighbourForZero(i + 1, j);
                }
                if (checkZero(i - 1, j)) {
                    checkNeighbourForZero(i - 1, j);
                }
                if (checkZero(i, j + 1)) {
                    checkNeighbourForZero(i, j + 1);
                }
                if (checkZero(i, j - 1)) {
                    checkNeighbourForZero(i, j - 1);
                }

            }

            private void fillOuter(ItemStack stack) {
                gui.fillColumn(0, stack);
                gui.fillColumn(1, stack);
                gui.fillColumn(7, stack);
                gui.fillColumn(8, stack);
                gui.fillPanel();
            }

            private void stopGame(State state) {
                if (this.state != State.PLAYING) {
                    return;
                }

                this.state = state;
                this.finishedAt = System.currentTimeMillis();

                revealAll();
            }

            // Ok it's iterating over 25 elements; it's fine.
            @Nullable
            private Mine getMineBySlot(int slot) {
                for (Mine[] mines : field) {
                    for (Mine mine : mines) {
                        if (mine.getSlot() == slot) {
                            return mine;
                        }
                    }
                }

                return null;
            }

        };
    }
}
