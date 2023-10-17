package me.hapyl.mmu3.feature.statechanger.adapter;

import com.google.common.collect.Maps;
import me.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;

import javax.annotation.Nonnull;
import java.util.Map;

public abstract class BlockFaceAdapter<T extends BlockData> extends Adapter<T> {

    private final Map<BlockFace, Integer> faceToSlotMap = Maps.newHashMap();

    public BlockFaceAdapter(@Nonnull Class<T> clazz) {
        super(clazz);

        faceToSlotMap.put(BlockFace.NORTH, 13);
        faceToSlotMap.put(BlockFace.SOUTH, 31);
        faceToSlotMap.put(BlockFace.WEST, 21);
        faceToSlotMap.put(BlockFace.EAST, 23);
        faceToSlotMap.put(BlockFace.UP, 4);
        faceToSlotMap.put(BlockFace.DOWN, 40);
        faceToSlotMap.put(BlockFace.NORTH_EAST, 14);
        faceToSlotMap.put(BlockFace.NORTH_WEST, 12);
        faceToSlotMap.put(BlockFace.SOUTH_EAST, 32);
        faceToSlotMap.put(BlockFace.SOUTH_WEST, 30);
    }

    @Nonnull
    public String getFaceName(@Nonnull BlockFace face) {
        return Chat.capitalize(face);
    }

    public boolean isUnknownFace(@Nonnull BlockFace face) {
        return !faceToSlotMap.containsKey(face);
    }

    public int getSlot(@Nonnull BlockFace face) {
        return faceToSlotMap.getOrDefault(face, -1);
    }
}
