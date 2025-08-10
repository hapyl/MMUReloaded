package me.hapyl.mmu3.feature.candle;

import com.google.common.collect.Maps;
import me.hapyl.eterna.module.entity.Entities;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.inventory.ItemEventHandler;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;
import me.hapyl.mmu3.message.Message;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class CandleController extends Feature implements Listener {

    private static final String candleTag = "__candle__";

    private final Map<UUID, Data> playerData;

    private final ItemStack CANDLE_ITEM = new ItemBuilder(Material.TORCH, Key.ofString("mmu3_candle"))
            .setName("Candle")
            .setItemModel(Material.CANDLE)
            .addTextBlockLore("""
                    Place on a block to create a candle.
                    
                    &8&o;;You can break the torch block of the candle to remove the candle.
                    
                    &8◦ &eLeft-Click on air to change texture
                    """)
            .setEventHandler(new ItemEventHandler() {
                @Override
                public void onLeftClick(@Nonnull Player player, @Nonnull PlayerInteractEvent ev) {
                    new CandleGUI(player);

                    ev.setCancelled(true);
                }

                @Override
                public void onBlockPlace(@Nonnull Player player, @Nonnull BlockPlaceEvent ev) {
                    final Block block = ev.getBlock();
                    final Location location = block.getLocation();
                    final Data data = getData(player);

                    if (data.isOffset()) {
                        location.setYaw(ThreadLocalRandom.current().nextFloat() * 160.0f);
                    }

                    // Spawn armor stand
                    Entities.ARMOR_STAND_MARKER.spawn(
                            location.add(0.5d, -1.5d, 0.5d), self -> {
                                self.setMarker(true);
                                self.setInvisible(true);
                                self.setSilent(true);
                                self.addScoreboardTag(candleTag);
                                self.getEquipment().setHelmet(data.getCandle().getItem());
                            }, Main.entityCache()
                    );
                }
            })
            .glow()
            .build();

    public CandleController(Main mmu3plugin) {
        super(mmu3plugin);
        playerData = Maps.newHashMap();
    }

    @EventHandler()
    public void handleBlockBreakEvent(BlockBreakEvent ev) {
        final Player player = ev.getPlayer();
        final Block block = ev.getBlock();

        if (block.getType() != Material.TORCH) {
            return;
        }

        final Location location = block.getLocation().add(0.5, 0, 0.5);
        final World world = location.getWorld();

        final Entity candle = world
                .getNearbyEntities(location, 0.51, 2.0, 0.51)
                .stream()
                .filter(entity -> entity instanceof ArmorStand stand && stand.getScoreboardTags().contains("__candle__"))
                .min(Comparator.comparingDouble(e -> e.getLocation().distanceSquared(location)))
                .orElse(null);

        if (candle != null) {
            Message.info(player, "Removed the candle!");
            Message.sound(player, Sound.ITEM_SHIELD_BREAK, 2.0f);

            candle.remove();
        }
    }

    @Nonnull
    public Data getData(Player player) {
        return playerData.computeIfAbsent(player.getUniqueId(), a -> new Data(player));
    }

    public void giveItem(Player player) {
        player.getInventory().addItem(CANDLE_ITEM);
    }

    public boolean hasItem(Player player) {
        return player.getInventory().contains(CANDLE_ITEM);
    }
}
