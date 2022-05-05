package me.hapyl.mmu3.feature.statechanger;

import com.google.common.collect.Maps;
import org.bukkit.block.BlockFace;

import java.util.Map;

public final class StateConstants {

    public static final Map<BlockFace, Integer> FACE_SLOT_MAP = Maps.newHashMap();

    static {
        FACE_SLOT_MAP.put(BlockFace.NORTH, 13);
        FACE_SLOT_MAP.put(BlockFace.SOUTH, 31);
        FACE_SLOT_MAP.put(BlockFace.WEST, 21);
        FACE_SLOT_MAP.put(BlockFace.EAST, 23);
        FACE_SLOT_MAP.put(BlockFace.UP, 4);
        FACE_SLOT_MAP.put(BlockFace.DOWN, 40);
        FACE_SLOT_MAP.put(BlockFace.NORTH_EAST, 14);
        FACE_SLOT_MAP.put(BlockFace.NORTH_WEST, 12);
        FACE_SLOT_MAP.put(BlockFace.SOUTH_EAST, 32);
        FACE_SLOT_MAP.put(BlockFace.SOUTH_WEST, 30);
    }


}
