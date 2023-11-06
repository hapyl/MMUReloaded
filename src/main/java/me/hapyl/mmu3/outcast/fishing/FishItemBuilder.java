package me.hapyl.mmu3.outcast.fishing;

import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import java.util.UUID;

public class FishItemBuilder extends ItemBuilder {

    public static final NbtData<Integer> NBT_FISH_SIZE = new NbtData<>("FishSize", PersistentDataType.INTEGER);
    public static final NbtData<String> NBT_FISH_UUID = new NbtData<>("FishUuid", PersistentDataType.STRING);
    public static final NbtData<String> NBT_CATCHER = new NbtData<>("Catcher", PersistentDataType.STRING);
    public static final NbtData<String> NBT_CATCHER_UUID = new NbtData<>("CatcherUuid", PersistentDataType.STRING);

    private final FishItem item;

    public FishItemBuilder(@Nonnull Material material) {
        super(material);

        this.item = new FishItem(material);
        super.item = this.item;
    }

    public void setFishSize(int size) {
        NBT_FISH_SIZE.set(this, size);

        item.size = size;
    }

    public void setFishUuid(@Nonnull UUID uuid) {
        NBT_FISH_UUID.set(this, uuid.toString());

        item.uuid = uuid;
    }

    public void setCatcher(@Nonnull Player player) {
        NBT_CATCHER.set(this, player.getName());
        NBT_CATCHER_UUID.set(this, player.getUniqueId().toString());

        item.catcher = player;
        item.catcherUuid = player.getUniqueId();
    }

    @Override
    public FishItem build() {
        return (FishItem) super.build();
    }
}
