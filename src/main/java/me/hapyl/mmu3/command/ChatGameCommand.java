package me.hapyl.mmu3.command;

import me.hapyl.mmu3.outcast.chatgame.ChatGames;
import me.hapyl.eterna.module.command.SimplePlayerAdminCommand;
import org.bukkit.entity.Player;

public class ChatGameCommand extends SimplePlayerAdminCommand {
    public ChatGameCommand(String name) {
        super(name);

        setDescription("Allows to start chat game.");
        addCompleterValues(1, ChatGames.values());
    }

    @Override
    protected void execute(Player player, String[] args) {
        //
    }
}
