package me.hapyl.mmu3.feature.statechanger;

import me.hapyl.eterna.module.player.PlayerLib;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

public class Data {

    private final Player player;
    private final Block block;
    private final BlockData originalBlockData;

    public Data(Player player, Block block) {
        this.player = player;
        this.block = block;
        this.originalBlockData = this.block.getBlockData().clone();
    }

    public void applyData(BlockData data) {
        this.block.setBlockData(data, false);
        PlayerLib.playSound(player, Sound.UI_BUTTON_CLICK, 2.0F);
    }

    public void applyData(BlockData data, StateChangerGUI gui) {
        applyData(data);
        gui.updateInventory();
    }

    public BlockData getBlockData() {
        return block.getBlockData();
    }

    public Player getPlayer() {
        return player;
    }

    public Block getBlock() {
        return block;
    }

    public void restoreOriginalBlockData() {
        this.block.setBlockData(getOriginalBlockData(), false);
    }

    public BlockData getOriginalBlockData() {
        return originalBlockData;
    }
}
