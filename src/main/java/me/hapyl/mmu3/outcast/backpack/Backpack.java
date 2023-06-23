package me.hapyl.mmu3.outcast.backpack;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.config.Config;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.nbt.NBT;
import me.hapyl.spigotutils.module.nbt.NBTType;
import me.hapyl.spigotutils.module.util.Validate;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Backpack extends Config {

    private final UUID uuid;
    private final BackpackSize size;

    private UUID owner;
    protected ItemStack[] contents;

    private Backpack(@Nonnull UUID uuid) {
        this(uuid, null);
    }

    private Backpack(@Nonnull UUID uuid, @Nullable BackpackSize size) {
        super(Main.getPlugin(), "/backpack", uuid.toString());

        this.uuid = uuid;
        this.size = size == null ? loadSize() : size;
        this.contents = new ItemStack[this.size.getSizeScaled()];

        loadItems();
    }


    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public ItemStack createItem(Player player) {
        return ItemBuilder
                .playerHeadUrl("2308bf5cc3e9decaf0770c3fdad1e042121cf39cc2505bbb866e18c6d23ccd0c")
                .setName("%s Backpack", Chat.capitalize(size))
                .setSmartLore("A backpack capable of holding up to %s items.".formatted(size.getSizeScaled()))
                .setPersistentData("BackpackId", NBTType.STR.getType(), uuid.toString())
                .setPersistentData("BackpackOwner", NBTType.STR.getType(), player.getUniqueId().toString())
                .build();
    }

    private BackpackSize loadSize() {
        return Validate.getEnumValue(BackpackSize.class, getString("size", "SMALL"), BackpackSize.SMALL);
    }

    public ItemStack[] getContents() {
        return contents;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void open(Player player) {
        new BackpackGUI(this, player);
    }

    private void loadItems() {
        final YamlConfiguration cfg = getConfig();

        for (int slot = 0; slot < this.size.getSizeScaled(); slot++) {
            final ItemStack stack = cfg.getItemStack("contents." + slot);
            if (stack == null) {
                continue;
            }

            this.contents[slot] = stack;
        }
    }

    protected void saveItems() {
        for (int slot = 0; slot < contents.length; slot++) {
            // set null items as well
            set("contents." + slot, contents[slot]);
        }

        set("size", this.size.name());
        set("owner", this.owner == null ? null : this.owner.toString());
        save();
    }

    public static boolean isBackpackItem(ItemStack item) {
        if (item == null) {
            return false;
        }

        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }

        return NBT.hasNbt(meta, "BackpackId", NBTType.STR.getType());
    }

    public static boolean isBackpackExists(UUID uuid) {
        return new File(Main.getPlugin().getDataFolder() + "/backpack", uuid.toString() + ".yml").exists();
    }

    public static Backpack createBackpack(BackpackSize size) {
        final UUID uuid = UUID.randomUUID();
        if (isBackpackExists(uuid)) {
            return createBackpack(size);
        }

        return new Backpack(uuid, size);
    }

    @Nullable
    public static Backpack loadFromUUID(UUID uuid) {
        if (!isBackpackExists(uuid)) {
            return null;
        }

        return new Backpack(uuid);
    }

    @Nullable
    public static Backpack loadFromItemStack(ItemStack item) {
        if (item == null) {
            return null;
        }

        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return null;
        }

        final String backpackId = NBT.getString(meta, "BackpackId");
        if (backpackId == null) {
            return null;
        }

        UUID backpackUuid;

        try {
            backpackUuid = UUID.fromString(backpackId);
        } catch (Exception e) {
            return null;
        }

        if (!isBackpackExists(backpackUuid)) {
            return null;
        }

        return new Backpack(backpackUuid);
    }


    public void delete(Player player, boolean returnItems) {
        try {
            FileUtils.forceDelete(getFile());
        } catch (IOException e) {
            Message.error(player, "Error deleting backpack! Check console.");
            e.printStackTrace();
            return;
        }

        Message.success(player, "Successfully deleted backpack! (%s)", uuid.toString());

        if (!returnItems) {
            return;
        }
        final PlayerInventory inventory = player.getInventory();
        for (ItemStack content : contents) {
            if (content == null) {
                continue;
            }
            inventory.addItem(content);
        }

        contents = null;
    }

    public BackpackSize getSize() {
        return size;
    }

    public String getName() {
        return Chat.capitalize(size) + " Backpack";
    }
}