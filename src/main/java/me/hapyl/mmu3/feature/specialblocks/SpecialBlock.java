package me.hapyl.mmu3.feature.specialblocks;

import kz.hapyl.spigotutils.module.inventory.ItemBuilder;
import kz.hapyl.spigotutils.module.math.Numbers;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.Message;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Snow;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

public class SpecialBlock {

    private static final String[] SPECIAL_BLOCK_INFO = { "&8Special Block",
                                                         "Place to transform into %s&7.",
                                                         "Use &e/specialblocks &7to browse all special blocks." };
    private final int slot;
    private final String name;
    private final Material icon;
    private final Material block;
    private final boolean glow;
    private final int amount;
    private final String id;
    private final ItemStack itemIcon;
    private final ItemStack itemReal;

    public SpecialBlock(int slot, Material icon, Material block, String name, String id, int amount, boolean glow) {
        this.slot = slot;
        this.name = ChatColor.GREEN + name;
        this.icon = icon;
        this.block = block;
        this.glow = glow;
        this.amount = amount;

        this.id = "sb." + id;

        this.itemIcon = new ItemBuilder(icon)
                .setName(this.name)
                .setAmount(amount)
                .addSmartLore(SPECIAL_BLOCK_INFO[0])
                .addSmartLore(SPECIAL_BLOCK_INFO[1].formatted(this.name))
                .addLore().addSmartLore(SPECIAL_BLOCK_INFO[2])
                .predicate(glow, ItemBuilder::glow)
                .build();

        this.itemReal = new ItemBuilder(block, this.id)
                .setName(this.name)
                .addSmartLore(SPECIAL_BLOCK_INFO[0])
                .addSmartLore(SPECIAL_BLOCK_INFO[1].formatted(this.name))
                .addLore().addSmartLore(SPECIAL_BLOCK_INFO[2])
                .predicate(glow, ItemBuilder::glow)
                .build();
    }

    public void acceptEvent(BlockPlaceEvent event) {
        final BlockData data = icon.createBlockData();
        setBlock(event.getBlockReplacedState(), data instanceof Bisected b ? topOrBottom(b) : data);
    }

    public String getId() {
        return id;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getIcon() {
        return itemIcon;
    }

    public ItemStack getItem() {
        return itemReal;
    }

    public void setBlock(BlockState state, Material material) {
        runLater(() -> state.getBlock().setType(material, false));
    }

    public void setBlock(BlockState state, BlockData data) {
        runLater(() -> {
            state.getBlock().setType(data.getMaterial(), false);
            state.getBlock().setBlockData(data, false);
        });
    }

    public void setLevelled(BlockPlaceEvent event, Material material, int lvl) {
        final BlockState state = event.getBlockReplacedState();
        final BlockData data = material.createBlockData();

        setBlock(state, material);
        if (data instanceof Snow snow) {
            snow.setLayers(Numbers.clamp(lvl, snow.getMinimumLayers(), snow.getMaximumLayers()));
        }

        if (data instanceof Levelled levelled) {
            levelled.setLevel(Numbers.clamp(lvl, 0, levelled.getMaximumLevel()));
        }

        setBlock(state, data);
    }

    public void setDoor(BlockPlaceEvent event, Material material, Bisected.Half half) {
        final BlockState state = event.getBlockReplacedState();
        final BlockData data = material.createBlockData();

        setBlock(state, material);
        if (data instanceof Door door) {
            door.setOpen(false);
            door.setHalf(half);
        }

        if (data instanceof Directional directional) {
            directional.setFacing(directional.getFacing());
        }

        setBlock(state, data);
    }

    public void giveItem(Player player) {
        final PlayerInventory inventory = player.getInventory();
        if (inventory.firstEmpty() == -1) {
            Message.error(player, "You don't have enough space in your inventory!");
            Message.sound(player, Sound.ENTITY_VILLAGER_NO);
            return;
        }

        inventory.addItem(this.itemReal);
        Message.success(player, "Gave %s special block to you.", name);
        Message.sound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 2.0f);
    }

    private BlockData topOrBottom(Bisected data) {
        data.setHalf(glow ? Bisected.Half.TOP : Bisected.Half.BOTTOM);
        return data;
    }

    private void runLater(Runnable runnable) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskLater(Main.getInstance(), 1L);
    }
}
