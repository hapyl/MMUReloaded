package me.hapyl.mmu3.game.games.wordle;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class WordleCollector {

    private final List<Character> hiddenWordChars;
    private final Map<Character, CharacterValue> mappedValues;
    private final List<String> guessedWords;

    public WordleCollector(String hiddenWord) {
        hiddenWordChars = Lists.newArrayList();
        for (char c : hiddenWord.toCharArray()) {
            hiddenWordChars.add(Character.toUpperCase(c));
        }
        mappedValues = Maps.newHashMap();
        guessedWords = Lists.newArrayList();
    }

    public boolean hasGuessed(String string) {
        return guessedWords.contains(string.toLowerCase());
    }

    public void addGuessed(String string) {
        guessedWords.add(string.toLowerCase());

        final char[] stringChars = string.toCharArray();

        for (int i = 0; i < 5; i++) {
            final char key = Character.toUpperCase(stringChars[i]);
            if (hiddenWordChars.get(i) == key) {
                mappedValues.put(key, CharacterValue.CORRECT);
            }
            else if (hiddenWordChars.contains(key)) {
                mappedValues.put(key, CharacterValue.PRESENT);
            }
            else {
                mappedValues.put(key, CharacterValue.INCORRECT);
            }
        }
    }

    public String getCharColor(char c) {
        final CharacterValue value = mappedValues.get(c);
        if (value == null) {
            return "&f&l";
        }
        return switch (value) {
            case CORRECT -> "&a&l";
            case INCORRECT -> "&8";
            case PRESENT -> "&e&l";
        };
    }

    public boolean hasLost() {
        return guessedWords.size() >= 6;
    }

    public List<String> getGuessedWords() {
        return guessedWords;
    }
}
