package me.hapyl.mmu3.game.games.wordle;

public enum CharacterValue {

    CORRECT,
    INCORRECT,
    PRESENT;

    public static CharacterValue[] calculateValues(String hiddenWord, char[] chars) {
        final char[] hiddenChars = hiddenWord.toCharArray();
        final CharacterValue[] values = new CharacterValue[5];

        for (int i = 0; i < hiddenChars.length; i++) {
            final char c = chars[i];
            if (hiddenChars[i] == c) {
                values[i] = CharacterValue.CORRECT;
            }
            else if (hiddenWord.contains(Character.toString(c).toLowerCase())) {
                values[i] = CharacterValue.PRESENT;
            }
            else {
                values[i] = CharacterValue.INCORRECT;
            }
        }

        return values;
    }

}
