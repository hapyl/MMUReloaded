package me.hapyl.mmu3.utils.nbt;

import com.google.common.collect.Maps;
import me.hapyl.spigotutils.module.annotate.TestedNMS;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.nbt.nms.NBTNative;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.Map;

@TestedNMS(version = "1.19.2")
public final class NBTHolder {

    private final String name;
    private final NBTTagCompound nbt;
    private final Map<String, NBTBase> mapped;

    public NBTHolder(ItemStack stack) {
        this(Chat.capitalize(stack.getType()), NBTNative.getCompound(stack));
    }

    public NBTHolder(Entity entity) {
        this(entity.getName(), null);
    }

    public NBTHolder(Block block) {
        this(Chat.capitalize(block.getType()), null);
    }

    private NBTHolder(String name, NBTTagCompound nbt) {
        this.name = name;
        this.nbt = nbt;
        this.mapped = getMapCopy();
    }

    public boolean hasData() {
        return !mapped.isEmpty();
    }

    public String getName() {
        return name;
    }

    public Map<String, NBTBase> getMappedValues() {
        return mapped;
    }

    @Nullable
    public NBTData getNbt(String s) {
        if (mapped.isEmpty()) {
            return null;
        }

        for (String key : mapped.keySet()) {
            if (key.equalsIgnoreCase(s)) {
                return new NBTData(key, mapped.get(key));
            }
        }

        return null;
    }

    @SuppressWarnings("all")
    private Map<String, NBTBase> getMapCopy() {
        try {
            return Maps.newHashMap((Map<String, NBTBase>) FieldUtils.readField(nbt, "x", true));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return Maps.newHashMap();
    }

}
