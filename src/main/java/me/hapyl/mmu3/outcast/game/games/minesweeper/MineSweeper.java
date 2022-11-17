package me.hapyl.mmu3.outcast.game.games.minesweeper;

import com.google.common.collect.Lists;
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

            private final long startedAt = System.currentTimeMillis();
            private final Mine[][] field = new Mine[5][5];
            private final int[] validSlots = {
                    2, 3, 4, 5, 6,
                    11, 12, 13, 14, 15,
                    20, 21, 22, 23, 24,
                    29, 30, 31, 32, 33,
                    38, 39, 40, 41, 42
            };

            private final ChatColor[] dangerColor = {
                    ChatColor.GREEN, ChatColor.DARK_GREEN,
                    ChatColor.YELLOW, ChatColor.GOLD, ChatColor.RED
            };

            private long finishedAt;
            private State state;

            @Override
            public void onGameStart() {
                state = State.PLAYING;
                generateBombs();
                fillOuter(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("&f").build());
                updateItems();
            }

            private void generateBombs() {
                int slot = 0;
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        field[i][j] = new Mine(validSlots[slot++]);
                    }
                }

                // Get 5 bombs
                final List<Mine> list = Lists.newArrayList();
                for (Mine[] mines : field) {
                    list.addAll(Lists.newArrayList(mines));
                }

                Collections.shuffle(list);

                for (int i = 0; i < 5; i++) {
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
                if (i < 0 || j < 0 || i >= 5 || j >= 5) {
                    return false;
                }

                return field[i][j].isBomb();
            }

            private void updateItems() {
                for (Mine[] mines : field) {
                    for (final Mine mine : mines) {
                        final int slot = mine.getSlot();
                        if (!mine.isRevealed()) {
                            setItem(slot, new ItemBuilder(mine.isMarked() ? Material.FILLED_MAP : Material.MAP).setName("&a???")
                                    .addLore("&eClick to reveal.")
                                    .addLore("&6Right Click to mark.")
                                    .addLoreIf("&4&lDEBUG &f" + (mine.isBomb() ? "&cMine" : "&a" + mine.getNearbyMines()), isDebug())
                                    .build());
                            continue;
                        }
                        if (mine.isBomb()) {
                            setItem(slot, new ItemBuilder(Material.TNT).setName("&c&lBOOM!").build());
                            continue;
                        }

                        final int nearbyMines = mine.getNearbyMines();
                        final ChatColor color = dangerColor[nearbyMines];
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
                    final String gameTime = Chat.formatTimeString(finishedAt - startedAt);
                    Chat.broadcast("&a%s &7completed the game in &a%s!", player.getName(), gameTime);
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
                    if (mine.isBomb()) {
                        stopGame(State.LOST);
                        PlayerLib.playSound(player, Sound.ENTITY_GENERIC_EXPLODE, 0.75f);
                        revealAll();
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
                fillOuter(ItemBuilder
                                  .of(Material.GREEN_STAINED_GLASS_PANE, "&aYou won!", "")
                                  .addLore("&7It took you %s!", Chat.formatTimeString(finishedAt - startedAt))
                                  .addLore()
                                  .addLore("&eClick to flex your time!")
                                  .build());
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
