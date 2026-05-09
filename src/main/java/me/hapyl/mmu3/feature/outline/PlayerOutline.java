package me.hapyl.mmu3.feature.outline;

import me.hapyl.mmu3.InternalTasks;
import me.hapyl.mmu3.MMULogger;
import me.hapyl.mmu3.util.PacketUtils;
import net.kyori.adventure.text.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class PlayerOutline {
    
    private static final int MAX_DISTANCE = 30;
    private static final int MAX_BOUNDING_BOX_SIZE = 48;
    
    private final Player player;
    
    @Nullable private Location start;
    @Nullable private Location end;
    
    @Nullable private BlockPos structureBlockPosition;
    
    public PlayerOutline(@NotNull Player player) {
        this.player = player;
    }
    
    @Nullable
    public Location getStart() {
        return start;
    }
    
    @Nullable
    public Location getEnd() {
        return end;
    }
    
    public boolean isDefined() {
        return start != null && end != null;
    }
    
    public void setStartFromTargetBlock() {
        setPositionFromTargetBlock(true);
    }
    
    public void setEndFromTargetBlock() {
        setPositionFromTargetBlock(false);
    }
    
    public int sizeX() {
        return sizeOf(Location::getX);
    }
    
    public int sizeY() {
        return sizeOf(Location::getY);
    }
    
    public int sizeZ() {
        return sizeOf(Location::getZ);
    }
    
    public int minX() {
        return minOf(Location::getX);
    }
    
    public int minY() {
        return minOf(Location::getY);
    }
    
    public int minZ() {
        return minOf(Location::getZ);
    }
    
    public int maxX() {
        return maxOf(Location::getX);
    }
    
    public int maxY() {
        return maxOf(Location::getY);
    }
    
    public int maxZ() {
        return maxOf(Location::getZ);
    }
    
    public void show() {
        if (start == null || end == null) {
            throw new IllegalStateException("Undefined start or end position!");
        }
        
        final int sizeX = sizeX();
        final int sizeY = sizeY();
        final int sizeZ = sizeZ();
        
        if (sizeX > MAX_BOUNDING_BOX_SIZE || sizeY > MAX_BOUNDING_BOX_SIZE || sizeZ > MAX_BOUNDING_BOX_SIZE) {
            MMULogger.error(player, Component.text("Could not draw outline because it exceeds the maximum size of %s!".formatted(MAX_BOUNDING_BOX_SIZE)));
            return;
        }
        
        // Always hide before showing again
        this.hide();
        
        InternalTasks.later(() -> {
            final BlockState blockState = Blocks.STRUCTURE_BLOCK.defaultBlockState();
            final World world = start.getWorld();
            
            final int x = minX();
            final int y = Math.max(minY() - 10, world.getMinHeight());
            final int z = minZ();
            
            final int offsetY = minY() - y;
            
            this.structureBlockPosition = new BlockPos(x, y, z);
            
            final CompoundTag nbt = new CompoundTag();
            nbt.putString("name", "outline:" + player.getName());
            nbt.putString("author", player.getName());
            nbt.putInt("posX", 0);
            nbt.putInt("posY", offsetY); // Compensate Y offset
            nbt.putInt("posZ", 0);
            nbt.putInt("sizeX", sizeX);
            nbt.putInt("sizeX", sizeY);
            nbt.putInt("sizeX", sizeZ);
            nbt.putString("rotation", "NONE");
            nbt.putString("mirror", "NONE");
            nbt.putString("mode", "SAVE");
            nbt.putByte("ignoreEntities", (byte) 1);
            nbt.putByte("showboundingbox", (byte) 1);
            
            // Create packets
            final ClientboundBlockUpdatePacket packetBlockUpdate = new ClientboundBlockUpdatePacket(structureBlockPosition, blockState);
            final ClientboundBlockEntityDataPacket packetBlockEntityData = new ClientboundBlockEntityDataPacket(structureBlockPosition, BlockEntityType.STRUCTURE_BLOCK, nbt);
            
            PacketUtils.sendPacket(player, packetBlockUpdate);
            PacketUtils.sendPacket(player, packetBlockEntityData);
        }, 2);
    }
    
    public void hide() {
        if (start == null || structureBlockPosition == null) {
            return;
        }
        
        // In order to hide the structure block we simply send the real block data to the player
        final Location structureBlockLocation = new Location(start.getWorld(), structureBlockPosition.getX(), structureBlockPosition.getY(), structureBlockPosition.getZ());
        
        player.sendBlockChange(structureBlockLocation, start.getBlock().getBlockData());
    }
    
    @NotNull
    public String getStartString() {
        return locationToString(start);
    }
    
    @NotNull
    public String getEndString() {
        return locationToString(end);
    }
    
    private int minOf(@NotNull Function<Location, Double> locationFunction) {
        if (start == null || end == null) {
            return 0;
        }
        
        return (int) Math.round(Math.min(locationFunction.apply(start), locationFunction.apply(end)));
    }
    
    private int maxOf(@NotNull Function<Location, Double> locationFunction) {
        if (start == null || end == null) {
            return 0;
        }
        
        return (int) Math.round(Math.max(locationFunction.apply(start), locationFunction.apply(end)));
    }
    
    private int sizeOf(@NotNull Function<Location, Double> locationFunction) {
        if (start == null || end == null) {
            return 0;
        }
        
        final double start = locationFunction.apply(this.start);
        final double end = locationFunction.apply(this.end);
        
        return (int) (Math.max(start, end) - Math.min(start, end)) + 1;
    }
    
    private void setPositionFromTargetBlock(boolean start) {
        final Location location = getTargetBlockLocation();
        
        if (location == null) {
            MMULogger.error(player, Component.text("Not looking at a block!"));
            return;
        }
        
        final Location locationCopy = locationCopy(location);
        final String locationString = locationToString(locationCopy);
        
        if (start) {
            this.start = locationCopy;
        }
        else {
            this.end = locationCopy;
        }
        
        
        MMULogger.success(player, Component.text("Set %s position to %s.".formatted(start ? "start" : "end", locationString)));
        MMULogger.sound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 2.0f);
    }
    
    @Nullable
    private Location getTargetBlockLocation() {
        final Block targetBlockExact = player.getTargetBlockExact(MAX_DISTANCE);
        
        return targetBlockExact != null ? targetBlockExact.getLocation() : null;
    }
    
    private static @NotNull Location locationCopy(@NotNull Location location) {
        return new Location(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }
    
    private static @NotNull String locationToString(@Nullable Location location) {
        return location != null ? "%.1f, %.1f, %.1f".formatted(location.getX(), location.getY(), location.getZ()) : "undefined";
    }
    
}