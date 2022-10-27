package me.hapyl.mmu3.utils.nbt;

import net.minecraft.nbt.NBTBase;

public class NBTData {

    private final String key;
    private final NBTBase value;

    public NBTData(String key, NBTBase nbt) {
        this.key = key;
        this.value = nbt;
    }

    public String getKey() {
        return key;
    }

    public NBTBase getValue() {
        return value;
    }
}
