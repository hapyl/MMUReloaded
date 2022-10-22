package me.hapyl.mmu3.outcast.game;

import me.hapyl.mmu3.outcast.game.games.CookieClicker;
import me.hapyl.mmu3.outcast.game.games.Ultrasequencer;
import me.hapyl.mmu3.outcast.game.games.benchmark.react.BenchmarkReaction;
import me.hapyl.mmu3.outcast.game.games.wordle.Wordle;
import org.bukkit.entity.Player;

public enum Games {

    COOKIE_CLICKER(new CookieClicker()),
    WORDLE(new Wordle()),
    REACTION_TIME(new BenchmarkReaction()),
    ULTRASEQUENCER(new Ultrasequencer()),

    ;

    private final Game game;

    Games(Game game) {
        this.game = game;
    }

    public String getName() {
        return game.getName();
    }

    public Game getGame() {
        return game;
    }

    public void startGame(Player player, Arguments arguments) {
        game.start(player, arguments);
    }

    public void startGame(Player player) {
        game.start(player, Arguments.empty());
    }

}
