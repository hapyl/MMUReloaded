package me.hapyl.mmu3.feature.dotcommand;

import com.google.common.collect.Maps;

import java.util.Map;

public class TranslateRussian {

    private final Map<Character, Character> russianToEnglishChars;

    public TranslateRussian() {
        russianToEnglishChars = Maps.newHashMap();
        russianToEnglishChars.put('й', 'q');
        russianToEnglishChars.put('ц', 'w');
        russianToEnglishChars.put('у', 'e');
        russianToEnglishChars.put('к', 'r');
        russianToEnglishChars.put('е', 't');
        russianToEnglishChars.put('н', 'y');
        russianToEnglishChars.put('г', 'u');
        russianToEnglishChars.put('ш', 'i');
        russianToEnglishChars.put('щ', 'o');
        russianToEnglishChars.put('з', 'p');
        russianToEnglishChars.put('х', '[');
        russianToEnglishChars.put('ъ', ']');
        russianToEnglishChars.put('ф', 'a');
        russianToEnglishChars.put('ы', 's');
        russianToEnglishChars.put('в', 'd');
        russianToEnglishChars.put('а', 'f');
        russianToEnglishChars.put('п', 'g');
        russianToEnglishChars.put('р', 'h');
        russianToEnglishChars.put('о', 'j');
        russianToEnglishChars.put('л', 'k');
        russianToEnglishChars.put('д', 'l');
        russianToEnglishChars.put('ж', ';');
        russianToEnglishChars.put('э', '\'');
        russianToEnglishChars.put('я', 'z');
        russianToEnglishChars.put('ч', 'x');
        russianToEnglishChars.put('с', 'c');
        russianToEnglishChars.put('м', 'v');
        russianToEnglishChars.put('и', 'b');
        russianToEnglishChars.put('т', 'n');
        russianToEnglishChars.put('ь', 'm');
        russianToEnglishChars.put('б', ',');
        russianToEnglishChars.put('ю', '.');
        russianToEnglishChars.put('.', '/');
    }

    public char getEnglishChar(char ru) {
        return russianToEnglishChars.getOrDefault(Character.toLowerCase(ru), ru);
    }

}
