package me.hapyl.mmu3.game.games;

import me.hapyl.mmu3.game.Arguments;
import me.hapyl.mmu3.game.Game;
import me.hapyl.mmu3.game.GameInstance;
import me.hapyl.spigotutils.module.inventory.gui.GUI;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class BenchmarkReaction extends Game {

    public BenchmarkReaction() {
        super("HB %s Reaction".formatted(GUI.ARROW_FORWARD));
    }

    @Nonnull
    @Override
    protected GameInstance newInstance(Player player, @Nonnull Arguments arguments) {
        return new GameInstance(player, this) {

            @Override
            public void onGameStart() {
            }

            @Override
            public void onClick(int slot) {

            }

        };
    }

}
