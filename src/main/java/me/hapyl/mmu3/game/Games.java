package me.hapyl.mmu3.game;

import me.hapyl.mmu3.game.wordle.Wordle;
import org.bukkit.entity.Player;

public enum Games {

    COOKIE_CLICKER(new CookieClicker()),
    WORDLE(new Wordle()),

    ;

    private final Game game;

    Games(Game game) {
        this.game = game;
    }

    public String getName() {
        return game.getName();
    }

    public void startGame(Player player, Arguments arguments) {
        game.start(player, arguments);
    }

    public void startGame(Player player) {
        game.start(player, Arguments.empty());
    }

}
