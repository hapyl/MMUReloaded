package me.hapyl.mmu3.outcast.chatgame;

import me.hapyl.mmu3.outcast.chatgame.games.ScramblerChatGame;

public enum ChatGames {

    SCRAMBLER(new ScramblerChatGame()),
    ;

    private final ChatGame game;

    ChatGames(ChatGame game) {
        this.game = game;
    }

    public ChatGame getGame() {
        return game;
    }
}
