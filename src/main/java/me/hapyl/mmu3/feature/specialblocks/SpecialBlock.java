package me.hapyl.mmu3.feature.specialblocks;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.inventory.ItemEventHandler;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.message.Message;
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

import javax.annotation.Nonnull;

public class SpecialBlock {

    public static final String KEY_PREFIX = "0special_block_";

    private static final String SPECIAL_BLOCK_INFO2 = """
            &8Special Block, %s
            
            Place to transform into &a%s&7!
            
            Use &e/specialBlocks&7 to browse all special blocks.
            
            &8;;Breaking a block while holding a special block will cause no updates.
            """;
    private final int slot;
    private final String name;
    private final Material icon;
    private final boolean glow;
    private final String id;
    private final ItemStack itemIcon;
    private final ItemStack itemReal;

    public SpecialBlock(Type type, int slot, Material icon, Material block, String name, String id, int amount, boolean glow) {
        this.slot = slot;
        this.name = ChatColor.GREEN + name;
        this.icon = icon;
        this.glow = glow;
        this.id = KEY_PREFIX + id;

        final String blockInfo = SPECIAL_BLOCK_INFO2.formatted(Chat.capitalize(type), this.name);

        this.itemIcon = new ItemBuilder(icon)
                .setName(this.name)
                .setAmount(amount)
                .addTextBlockLore(blockInfo)
                .predicate(glow, ItemBuilder::glow)
                .build();

        this.itemReal = new ItemBuilder(block, Key.ofString(this.id))
                .setName(this.name)
                .addTextBlockLore(blockInfo)
                .setEventHandler(new ItemEventHandler() {
                    @Override
                    public void onBlockPlace(@Nonnull Player player, @Nonnull BlockPlaceEvent ev) {
                        ev.setCancelled(true);
                        ev.setBuild(false);
                        SpecialBlock.this.acceptEvent(ev);
                    }
                })
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
            snow.setLayers(Math.clamp(lvl, snow.getMinimumLayers(), snow.getMaximumLayers()));
        }

        if (data instanceof Levelled levelled) {
            levelled.setLevel(Math.clamp(lvl, 0, levelled.getMaximumLevel()));
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

        if (data instanceof Directional directional && event.getBlock().getBlockData() instanceof Directional actualData) {
            directional.setFacing(actualData.getFacing());
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

    public static boolean isSpecialBlock(ItemStack handItem) {
        final Key key = ItemBuilder.getItemKey(handItem);

        return !key.isEmpty() && key.getKey().startsWith(KEY_PREFIX);
    }
}
