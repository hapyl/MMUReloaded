package me.hapyl.mmu3.feature.brush;

import com.google.common.collect.Sets;
import me.hapyl.spigotutils.module.math.Numbers;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerBrush {

    private static final int MAX_QUEUE_SIZE = 100;

    private final LinkedList<BrushOperation> operationQueue;
    private final UUID uuid;
    private final Set<Material> mask;

    private Brush brush;
    private Material brushItem;
    private Material material;
    private int size;

    public PlayerBrush(UUID player) {
        this.uuid = player;
        this.brush = Brush.NONE;
        this.brushItem = Material.WOODEN_HOE;
        this.material = Material.AIR;
        this.mask = Sets.newHashSet();
        this.operationQueue = new LinkedList<>();
        this.size = 2;
    }

    public void useBrush(Location location) {
        final Collection<Block> collect = brush.getPattern().collect(location, size);
        final BrushOperation operation = new BrushOperation();

        collect.forEach(block -> {
            if (!isMasked(block.getType())) {
                return;
            }

            operation.add(block);
            block.setType(material, true);
        });

        operationQueue.add(operation);
        if (operationQueue.size() >= MAX_QUEUE_SIZE) {
            operationQueue.pollLast();
        }
    }

    public int getQueueSize() {
        return operationQueue.size();
    }

    public int undo(int times) {
        times = Numbers.clamp(times, 1, 100);
        int undid = 0;
        if (operationQueue.isEmpty()) {
            return undid;
        }

        while (times-- > 0) {
            final BrushOperation element = operationQueue.poll();
            if (element == null) {
                break;
            }

            undid++;
            element.getBlocks().forEach(BlockInfo::restore);
        }
        return undid;
    }

    public void setMask(Material... block) {
        resetMask();
        mask.addAll(Arrays.asList(block));
    }

    public void addMask(Material block) {
        mask.add(block);
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

    public Set<Material> getMask() {
        return mask;
    }

    public boolean isMasked(Material material) {
        if (mask.isEmpty()) {
            return true;
        }
        return mask.contains(material);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setBrush(Brush brush) {
        this.brush = brush;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Brush getBrush() {
        return brush;
    }

    public Material getMaterial() {
        return material;
    }

    public int getSize() {
        return size;
    }
}
