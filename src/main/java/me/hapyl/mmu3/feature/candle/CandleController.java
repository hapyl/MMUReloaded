package me.hapyl.mmu3.feature.candle;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.Message;
import me.hapyl.mmu3.feature.Feature;
import me.hapyl.spigotutils.module.entity.Entities;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.util.Nulls;
import me.hapyl.spigotutils.module.util.ThreadRandom;
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
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class CandleController extends Feature implements Listener {

    private final Map<UUID, Data> playerData;
    private final String itemId = "mmu3_Candle";

    private final ItemStack CANDLE_ITEM = new ItemBuilder(Material.TORCH, itemId)
            .setName("&aCandle")
            .addSmartLore("Place on a block to create a candle. Left click to change candle texture.")
            .addClickEvent(CandleGUI::new, Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK)
            .glow()
            .build();

    public CandleController(Main mmu3plugin) {
        super(mmu3plugin);
        playerData = Maps.newHashMap();
    }

    @EventHandler()
    public void handleBlockPlaceEvent(BlockPlaceEvent ev) {
        final Player player = ev.getPlayer();
        final Block block = ev.getBlock();
        final ItemStack handItem = player.getInventory().getItemInMainHand();

        if (block.getType() != Material.TORCH || !ItemBuilder.itemHasID(handItem, itemId)) {
            return;
        }

        final Location location = block.getLocation();
        final Data data = getData(player);

        if (data.isOffset()) {
            location.setYaw(ThreadRandom.nextFloat() * 160.0f);
        }

        // spawn armor stand
        Entities.ARMOR_STAND_MARKER.spawn(location.add(0.5d, -1.5d, 0.5d), self -> {
            self.setInvisible(true);
            self.setSilent(true);
            self.addScoreboardTag("__candle__");
            Nulls.runIfNotNull(self.getEquipment(), equipment -> equipment.setHelmet(data.getCandle().getItem()));
        }, Main.getPlugin());
    }

    @EventHandler()
    public void handleBlockBreakEvent(BlockBreakEvent ev) {
        final Player player = ev.getPlayer();
        final Block block = ev.getBlock();

        if (block.getType() != Material.TORCH) {
            return;
        }

        final Location location = block.getLocation();
        final World world = location.getWorld();
        if (world == null) {
            return;
        }

        final Set<Entity> candles = world
                .getNearbyEntities(location, 1.0d, 2.0d, 1.0d)
                .stream()
                .filter(entity -> entity instanceof ArmorStand stand && stand.getScoreboardTags().contains("__candle__"))
                .collect(Collectors.toSet());

        if (!candles.isEmpty()) {
            Message.info(player, "Removed %s candles.", candles.size());
            Message.sound(player, Sound.ITEM_SHIELD_BREAK, 2.0f);
            candles.forEach(Entity::remove);
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
