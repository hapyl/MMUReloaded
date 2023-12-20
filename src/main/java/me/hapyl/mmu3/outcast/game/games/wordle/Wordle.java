package me.hapyl.mmu3.outcast.game.games.wordle;

import com.google.common.collect.Lists;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.outcast.game.Arguments;
import me.hapyl.mmu3.outcast.game.Game;
import me.hapyl.mmu3.outcast.game.GameInstance;
import me.hapyl.mmu3.utils.PanelGUI;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.inventory.Response;
import me.hapyl.spigotutils.module.inventory.SignGUI;
import me.hapyl.spigotutils.module.player.PlayerLib;
import me.hapyl.spigotutils.module.util.Runnables;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class Wordle extends Game {

    private final WordleDictionary dictionary;

    public Wordle() {
        super("Wordle", PanelGUI.Size.NO_PANEL);
        dictionary = new WordleDictionary();
    }

    private String randomWord() {
        return dictionary.getRandomWord();
    }

    private String getWord(int id) {
        if (id == -1) {
            return randomWord();
        }
        final String word = dictionary.getByIndex(id);
        return word == null ? randomWord() : word;
    }

    private boolean isValidWord(String str) {
        return dictionary.isValidWord(str);
    }

    @Nonnull
    @Override
    protected GameInstance newInstance(Player player, @Nonnull Arguments arguments) {
        return new GameInstance(player, this) {

            private final String hiddenWord = getWord(arguments.getInt(0, -1));
            private final int wordId = dictionary.getIndexOf(hiddenWord);
            private final WordleCollector collector = new WordleCollector(hiddenWord);

            private Status status = Status.GUESSING;

            private void updateWordleGuesses() {
                int line = 0;
                for (String word : collector.getGuessedWords()) {
                    final char[] chars = word.toCharArray();
                    final CharacterValue[] values = CharacterValue.calculateValues(hiddenWord, chars);
                    int slot = 2;

                    for (int i = 0; i < chars.length; i++) {
                        final char c = chars[i];
                        final WordleAlphabet wa = WordleAlphabet.byChar(c);
                        if (wa == null) {
                            gui.setItem(slot + (line * 9), new ItemStack(Material.BEDROCK));
                        }
                        else {
                            final CharacterValue value = values[i];
                            gui.setItem(
                                    slot + (line * 9),
                                    value == CharacterValue.CORRECT ? wa.getLetterCorrect() :
                                            value == CharacterValue.PRESENT ? wa.getLetterPresent() : wa.getLetterIncorrect()
                            );
                        }
                        ++slot;
                    }

                    // Check for win
                    if (hiddenWord.equalsIgnoreCase(word)) {
                        status = Status.WON;
                        fillBorders(Material.LIME_STAINED_GLASS_PANE);
                        setItem(
                                36,
                                new ItemBuilder(Material.MAP)
                                        .setName("&aYou Won!")
                                        .addLore("The word was &e%s&7!", hiddenWord)
                                        .addLore("")
                                        .addSmartLore("You guessed it within %s tries.".formatted(collector.getGuessedWords().size()))
                                        .addLore()
                                        .addLore("&eClick to flex")
                                        .build()
                        );
                        PlayerLib.playSound(getPlayer(), Sound.ENTITY_PLAYER_LEVELUP, 1.25f);
                    }

                    // Check for lose
                    else if (collector.hasLost()) {
                        status = Status.LOST;
                        fillBorders(Material.RED_STAINED_GLASS_PANE);
                        setItem(
                                36,
                                new ItemBuilder(Material.BARRIER)
                                        .setName("&aYou Lost!")
                                        .addLore("The word was &e%s&7!", hiddenWord)
                                        .addLore()
                                        .addLore("&eClick to cry")
                                        .build()
                        );
                        PlayerLib.playSound(getPlayer(), Sound.ENTITY_PLAYER_BURP, 0.0f);
                    }

                    line++;
                }

                if (collector.hasLost()) {
                    return;
                }

                // update sign with alphabet
                gui.setItem(36, buildGuessSign());
            }

            private void fillBorders(Material stack) {
                final ItemStack item = new ItemBuilder(stack).setName("&f").build();
                gui.fillColumn(0, item);
                gui.fillColumn(1, item);
                gui.fillColumn(7, item);
                gui.fillColumn(8, item);
            }

            @Override
            public void onClick(int slot, ClickType clickType) {
                if (slot != 36) {
                    return;
                }

                if (status == Status.WON) {
                    Chat.broadcast(
                            "&a&lWORDLE! &a%s &7guessed wordle &a#%s &7in &a%s &7tries.",
                            getPlayer().getName(),
                            wordId,
                            collector.getGuessedWords().size()
                    );

                    generateCubes().forEach(cube -> Chat.broadcast("  " + cube));
                    getPlayer().closeInventory();
                    return;
                }

                if (status == Status.LOST) {
                    getPlayer().closeInventory();
                    return;
                }

                status = Status.WAIT_RESPONSE;
                getPlayer().closeInventory();

                new SignGUI(getPlayer(), "Enter Five Letter Word") {
                    @Override
                    public void onResponse(Response response) {
                        final String word = response.getString(0);

                        if (word.length() != 5) {
                            Message.error(player, "A word must be 5 letter long!");
                            Message.sound(player, Sound.ENTITY_VILLAGER_NO);
                        }
                        else if (!isValidWord(word)) {
                            Message.error(player, "This word is not in dictionary!");
                            Message.sound(player, Sound.ENTITY_VILLAGER_NO);
                        }
                        else if (collector.hasGuessed(word)) {
                            Message.error(player, "You already guessed this word!");
                            Message.sound(player, Sound.ENTITY_VILLAGER_NO);
                        }
                        else {
                            collector.addGuessed(word);
                            Message.sound(player, Sound.ENTITY_VILLAGER_YES);
                        }

                        status = Status.GUESSING;
                        returnToMenuFromSign();
                    }
                };
            }

            private void returnToMenuFromSign() {
                Runnables.runSync(() -> {
                    updateWordleGuesses();
                    gui.openInventory();
                });
            }

            @Override
            public void onGameStart() {
                fillBorders(Material.BLACK_STAINED_GLASS_PANE);
                updateWordleGuesses();
            }

            public ItemStack buildGuessSign() {
                final ItemBuilder sign = new ItemBuilder(Material.OAK_SIGN)
                        .setName("&aGuess Word")
                        .addSmartLore("Click to enter 5 letter word you wish to guess.")
                        .addLore();

                // add alphabet
                StringBuilder builder = new StringBuilder();
                for (char c = 'A'; c <= 'Z'; c++) {
                    builder.append(collector.getCharColor(c)).append(c).append(" ");
                    if (c == 'I' || c == 'R' || c == 'Z') {
                        sign.addLore((c == 'Z' ? "        " : "       ") + builder.toString().trim());
                        builder = new StringBuilder();
                    }
                }

                return sign.addLore().addLore("&eClick to enter guess!").build();
            }

            @Override
            public boolean onGameStop() {
                if (status == Status.WAIT_RESPONSE) {
                    return false;
                }

                // if closed inventory while guessing = left
                if (status == Status.GUESSING) {
                    Message.info(getPlayer(), "Finished Wordle! The word was %s.", hiddenWord);
                }

                return true;
            }

            private List<String> generateCubes() {
                final List<String> cubes = Lists.newArrayList();
                StringBuilder builder = new StringBuilder();

                for (String word : collector.getGuessedWords()) {
                    final CharacterValue[] values = CharacterValue.calculateValues(hiddenWord, word.toCharArray());
                    for (CharacterValue value : values) {
                        builder.append(value.getColor()).append("■");
                    }
                    cubes.add(builder.toString());
                    builder = new StringBuilder();
                }
                return cubes;
            }

        };
    }

}
