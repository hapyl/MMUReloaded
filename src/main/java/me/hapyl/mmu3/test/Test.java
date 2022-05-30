package me.hapyl.mmu3.test;

import me.hapyl.mmu3.Main;
import me.hapyl.spigotutils.module.command.CommandProcessor;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Test {

    private final CommandProcessor processor;

    public Test() {
        processor = new CommandProcessor(Main.getPlugin());

        addTestCommand("timetohunt", (player, args) -> SkyWolf.test(player));
    }

    private void addTestCommand(String command, BiConsumer<Player, String[]> consumer) {
        processor.registerCommand(new SimplePlayerAdminCommand("test" + command) {
            @Override
            protected void execute(Player player, String[] strings) {
                consumer.accept(player, strings);
            }
        });
    }

    private void addTestCommand(String command, Consumer<Player> consumer) {
        addTestCommand(command, (player, strings) -> consumer.accept(player));
    }

}
