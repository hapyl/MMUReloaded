package me.hapyl.mmu3.command;

import me.hapyl.mmu3.MMULogger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MMUOperationWiki implements MMUOperation {

    private final String wikiUrl = "https://github.com/hapyl/MMUReloaded/wiki";

    @Override
    public @NotNull String name() {
        return "wiki";
    }

    @Override
    public @NotNull String description() {
        return "Displays the GitHub wiki link for the plugin.";
    }

    @Override
    public void process(@NotNull Player player, @NotNull ArgumentList args) {
        MMULogger.info(
                player,
                Component.empty()
                        .append(Component.text("Wiki link: ", NamedTextColor.WHITE))
                        .append(Component.text(wikiUrl, NamedTextColor.GOLD, TextDecoration.UNDERLINED))
                        .hoverEvent(HoverEvent.showText(Component.text("Click to open the url!", NamedTextColor.YELLOW)))
                        .clickEvent(ClickEvent.openUrl(wikiUrl))
        );
    }

}