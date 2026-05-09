package me.hapyl.mmu3.feature.candle;

import me.hapyl.mmu3.MMULogger;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.data.PlayerData;
import me.hapyl.mmu3.feature.Feature;
import me.hapyl.mmu3.feature.FeatureKey;
import me.hapyl.mmu3.util.ButtonComponents;
import me.hapyl.mmu3.util.ComponentHelper;
import me.hapyl.mmu3.util.ItemBuilder;
import me.hapyl.mmu3.util.LocationHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Random;

public class CandleFeature extends Feature implements Listener {
    
    private static final NamespacedKey CANDLE_ITEM_KEY = Main.createNamespacedKey("mmu3_candle_item");
    private static final NamespacedKey CANDLE_ENTITY_KEY = Main.createNamespacedKey("mmu3_candle_entity");
    
    private static final Random RANDOM = new Random();
    
    public CandleFeature(@NotNull Main plugin) {
        super(FeatureKey.create("candle"), plugin);
    }
    
    @EventHandler
    public void handleBlockPlaceEvent(BlockPlaceEvent ev) {
        final Player player = ev.getPlayer();
        final Block blockPlaced = ev.getBlock();
        
        if (isDisabled() || blockPlaced.getType() != Material.TORCH) {
            return;
        }
        
        final ItemStack itemInHand = ev.getItemInHand();
        
        if (!isCandleItem(itemInHand)) {
            return;
        }
        
        final PlayerData playerData = PlayerData.ofPlayer(player);
        final CandleData candleData = playerData.requestData(CandleData.class);
        
        final CandleTexture candle = candleData.getCandle();
        
        // If no texture is selected, open the candle menu
        if (candle == null) {
            new CandleMenu(player, candleData);
            return;
        }
        
        // Otherwise, create the candle entity
        final Location location = LocationHelper.center(blockPlaced.getLocation());
        
        // Apply random offset
        if (candleData.isRandomOffset()) {
            location.setYaw(RANDOM.nextFloat() * 360);
        }
        
        final Entity candleEntity = candleData.getCandleType().createEntity(location, candle);
        
        // Mark entity as candle, so we can remove entities even after reloads
        candleEntity.getPersistentDataContainer().set(CANDLE_ENTITY_KEY, PersistentDataType.BOOLEAN, true);
    }
    
    @EventHandler
    public void handleBlockBreakEvent(BlockBreakEvent ev) {
        final Player player = ev.getPlayer();
        final Block block = ev.getBlock();
        
        if (isDisabled() || block.getType() != Material.TORCH) {
            return;
        }
        
        final Location location = LocationHelper.center(block.getLocation());
        
        // Find the closest entity to the torch, because that's the best way of finding the candle, I think, I hope
        location.getWorld().getNearbyEntities(location, 1, 1, 1)
                .stream()
                .filter(entity -> entity.getPersistentDataContainer().has(CANDLE_ENTITY_KEY))
                .min(Comparator.comparingDouble(entity -> entity.getLocation().distanceSquared(location)))
                .ifPresent(entity -> {
                    // Manually set the block to AIR to prevent physics update
                    ev.setCancelled(true);
                    ev.setDropItems(false);
                    
                    block.setType(Material.AIR, false);
                    
                    entity.remove();
                    
                    MMULogger.info(player, Component.text("Remove candle entity."));
                    MMULogger.sound(player, Sound.ITEM_SHIELD_BLOCK, 2.0f);
                });
    }
    
    public void giveCandleItem(@NotNull Player player) {
        final PlayerInventory playerInventory = player.getInventory();
        
        // If player already has the item, don't give it again
        for (ItemStack itemStack : playerInventory.getContents()) {
            if (itemStack == null) {
                continue;
            }
            
            if (itemStack.getItemMeta().getPersistentDataContainer().has(CANDLE_ITEM_KEY, PersistentDataType.STRING)) {
                return;
            }
        }
        
        playerInventory.addItem(createCandleItem());
    }
    
    @NotNull
    public ItemStack createCandleItem() {
        return new ItemBuilder(Material.TORCH)
                .setItemModel(Material.CANDLE)
                .setPersistentData(CANDLE_ITEM_KEY, PersistentDataType.BOOLEAN, true)
                .setMaxStackSize(1)
                .setName(Component.text("Candle"))
                .addLore(Component.text("MMU Item", NamedTextColor.DARK_GRAY))
                .addLore()
                .addLore(ComponentHelper.wrap("Place on a block to create a candle."))
                .addLore()
                .addLore(ComponentHelper.wrap("Break an existing candle's torch block to remove it."))
                .addLore()
                .addLore(ButtonComponents.of(Component.text("Left-Click air to change texture")))
                .glow()
                .build();
    }
    
    private static boolean isCandleItem(@NotNull ItemStack itemStack) {
        final ItemMeta itemMeta = itemStack.getItemMeta();
        
        return itemMeta != null && itemMeta.getPersistentDataContainer().has(CANDLE_ITEM_KEY, PersistentDataType.STRING);
    }
    
}