package me.hapyl.mmu3.feature;

import com.google.common.collect.Sets;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.chat.LazyEvent;
import me.hapyl.spigotutils.module.util.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CommandBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.Set;

public class EmptyCommandBlockLocator extends Feature implements Listener {

    private final Set<Location> emptyCommandBlocks;

    public EmptyCommandBlockLocator(Main mmu3plugin) {
        super(mmu3plugin);

        emptyCommandBlocks = Sets.newHashSet();
    }

    public Set<Location> getEmptyCommandBlocks() {
        return emptyCommandBlocks;
    }

    @EventHandler()
    public void handleBlockBreak(BlockBreakEvent ev) {
        final Block block = ev.getBlock();
        final Player player = ev.getPlayer();

        switch (block.getType()) {
            case COMMAND_BLOCK, CHAIN_COMMAND_BLOCK, REPEATING_COMMAND_BLOCK -> {
                final Location location = block.getLocation();
                if (emptyCommandBlocks.contains(location)) {
                    emptyCommandBlocks.remove(location);
                    Message.info(player, "Removed %s from empty command blocks.", BukkitUtils.locationToString(location));
                }
            }
        }

    }

    @EventHandler()
    private void handleChunkLoad(ChunkLoadEvent ev) {
        final Set<Location> blocks = findEmptyCommandBlocks(ev.getChunk());

        if (blocks.isEmpty()) {
            return;
        }

        emptyCommandBlocks.addAll(blocks);

        Bukkit.getOnlinePlayers().stream().filter(Player::isOp).forEach(player -> {
            Message.severe(player, "Found %s empty Command Blocks in your world!", blocks.size());
            Message.clickHover(
                    player,
                    LazyEvent.runCommand("/findemptycommandblocks show"),
                    LazyEvent.showText("&aClick to find them!"),
                    "&e&lCLICK HERE &6to find them!"
            );
        });
    }

    public static Set<Location> findEmptyCommandBlocks(Chunk chunk) {
        final Set<Location> set = Sets.newHashSet();

        for (BlockState tileEntity : chunk.getTileEntities()) {
            if (tileEntity instanceof CommandBlock cb && (cb.getCommand().isEmpty() || cb.getCommand().isBlank())) {
                set.add(cb.getLocation());
            }
        }

        return set;
    }

}
