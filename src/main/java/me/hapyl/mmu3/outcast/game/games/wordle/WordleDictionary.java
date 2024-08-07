package me.hapyl.mmu3.outcast.game.games.wordle;

import com.google.common.collect.Lists;
import me.hapyl.mmu3.Main;
import me.hapyl.eterna.module.util.CollectionUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

public class WordleDictionary {

    private final List<String> words; // using map for indexing words
    private File file;
    private YamlConfiguration yaml;

    public WordleDictionary() {
        words = Lists.newArrayList();
        loadConfig();
        words.addAll(yaml.getStringList("words"));
    }

    public String getRandomWord() {
        return CollectionUtils.randomElement(words);
    }

    public boolean isValidWord(String word) {
        return words.contains(word.toLowerCase(Locale.ROOT));
    }

    public int getIndexOf(String word) {
        return words.lastIndexOf(word);
    }

    private void loadConfig() {
        file = new File(Main.getPlugin().getDataFolder(), "wordle.yml");
        final InputStream resource = Main.getPlugin().getResource("wordle.yml");

        // find yaml in plugin
        if (resource != null) {
            yaml = YamlConfiguration.loadConfiguration(new InputStreamReader(resource));
            yaml.options().copyDefaults(true);
        }

        try {
            yaml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getByIndex(int id) {
        return words.size() >= id ? words.get(id) : "index";
    }
}
