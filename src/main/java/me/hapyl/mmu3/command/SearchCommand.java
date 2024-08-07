package me.hapyl.mmu3.command;

import me.hapyl.mmu3.feature.search.SearchItemGUI;
import me.hapyl.mmu3.message.Message;
import me.hapyl.eterna.module.command.SimplePlayerAdminCommand;
import org.bukkit.entity.Player;

public class SearchCommand extends SimplePlayerAdminCommand {
    public SearchCommand(String name) {
        super(name);
        setDescription("Searches for an items by its name.");

        setAliases("lookup", "se");
    }

    @Override
    protected void execute(Player player, String[] args) {
        final String string = getArgument(args, 0).toString();

        if (string.isEmpty()) {
            Message.error(player, "Provide a query.");
            return;
        }

        new SearchItemGUI(player, string);
    }
}
