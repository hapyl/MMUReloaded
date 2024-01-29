package me.hapyl.mmu3.command;

import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.chat.LazyEvent;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import org.bukkit.entity.Player;

public class WikiCommand extends SimplePlayerAdminCommand {

    private final String url = "https://github.com/hapyl/MMUReloaded/wiki";

    public WikiCommand(String name) {
        super(name);

        setDescription("Gets the GitHub wiki link for the plugin.");
    }

    @Override
    protected void execute(Player player, String[] args) {
        Chat.sendClickableHoverableMessage(
                player,
                LazyEvent.openUrl(url),
                LazyEvent.showText("&eClick to open!"),
                "&6&lCLICK HERE&7 to open GitHub wiki."
        );
    }
}
