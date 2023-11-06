package me.hapyl.mmu3.feature.brush;

import com.google.common.collect.Sets;
import me.hapyl.mmu3.feature.UndoManager;
import me.hapyl.mmu3.feature.block.BlockChangeQueue;
import me.hapyl.mmu3.feature.block.MultiBlockChange;
import me.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public class PlayerBrush {

    private final UUID uuid;
    private final Set<Material> mask;
    private final BlockChangeQueue undoMap;
    protected long lastUndoUsage;

    private Brushes enumBrush;
    private Material brushItem;
    private Material material;
    private double size;

    public PlayerBrush(@Nonnull UUID player) {
        this.uuid = player;
        this.enumBrush = Brushes.NONE;
        this.brushItem = Material.WOODEN_HOE;
        this.material = Material.AIR;
        this.mask = Sets.newHashSet();
        this.undoMap = UndoManager.getUndoMap(uuid);
    }

    public void useBrush(@Nonnull Location location) {
        final Brush brush = enumBrush.getBrush();

        if (brush == null) {
            return;
        }

        final Collection<Block> collect = brush.collect(getPlayer(), NonNullWorldLocation.from(location), size);

        if (collect == null) {
            return;
        }

        final MultiBlockChange blockChange = new MultiBlockChange();
        collect.forEach(block -> {
            if (!isMasked(block.getType())) {
                return;
            }

            if (block.getType() == material) {
                return;
            }

            blockChange.add(block);
            block.setType(material, !brush.isCancelPhysics());
        });

        undoMap.add(blockChange);
    }

    public int undo(int times) {
        return undoMap.restore(times);
    }

    public Material getBrushItem() {
        return brushItem;
    }

    public void setBrushItem(Material material) {
        this.brushItem = material;
    }

    public void resetMask() {
        mask.clear();
    }

    @Nonnull
    public Set<Material> getMask() {
        return mask;
    }

    public void setMask(@Nonnull Set<Material> newMask) {
        mask.clear();
        mask.addAll(newMask);
    }

    public boolean isMasked(Material material) {
        if (mask.isEmpty()) {
            return true;
        }
        return mask.contains(material);
    }

    @Nonnull
    public Player getPlayer() {
        final Player player = Bukkit.getPlayer(uuid);

        if (player == null) {
            throw new IllegalStateException("Player is not online but used a brush?");
        }

        return player;
    }

    @Nonnull
    public UUID getUuid() {
        return uuid;
    }

    @Nonnull
    public Brushes getBrush() {
        return enumBrush;
    }

    public void setBrush(@Nonnull Brushes brush) {
        this.enumBrush = brush;
    }

    @Nonnull
    public Material getMaterial() {
        return material;
    }

    public void setMaterial(@Nonnull Material material) {
        this.material = material;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    @Nonnull
    public String getMaskFormatted() {
        if (mask.isEmpty()) {
            return "None!";
        }

        final StringBuilder builder = new StringBuilder();

        int index = 0;
        for (Material material : mask) {
            if (index++ != 0) {
                builder.append("&7, ");
            }

            builder.append(ChatColor.YELLOW).append(Chat.capitalize(material));
        }

        return builder.toString();
    }

    public boolean canUndo() {
        return !undoMap.isEmpty();
    }
}
