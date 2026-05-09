package me.hapyl.mmu3.feature.trim;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;
import me.hapyl.mmu3.MMULogger;
import org.bukkit.GameMode;
import org.bukkit.Input;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInputEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class TrimManager extends Feature implements Listener {

    private final Map<Player, TrimEditor> trimEditorMap = Maps.newHashMap();
    private final List<CachedTrimData> cachedTrims = Lists.newArrayList();

    public TrimManager(@NotNull Main plugin) {
        super(plugin);
    }

    @EventHandler
    public void handlePlayerInputEvent(PlayerInputEvent ev) {
        final Player player = ev.getPlayer();
        final Input input = ev.getInput();
        final TrimEditor editor = trimEditorMap.get(player);

        if (editor == null) {
            return;
        }

        // W & S
        if (input.isForward()) {
            editor.previousSlot();
        }
        else if (input.isBackward()) {
            editor.nextSlot();
        }
        // A & D
        else if (input.isLeft()) {
            editor.previousEntry();
        }
        else if (input.isRight()) {
            editor.nextEntry();
        }
        // Mode
        else if (input.isJump()) {
            editor.switchMode();
        }
        // Menu
        else if (input.isSneak()) {
            editor.openGUI();
        }
    }

    @EventHandler()
    public void handlePlayerInteractEvent(PlayerInteractEvent ev) {
        final Player player = ev.getPlayer();
        final Action action = ev.getAction();
        final EquipmentSlot hand = ev.getHand();

        if (hand == EquipmentSlot.OFF_HAND || action == Action.PHYSICAL) {
            return;
        }

        final TrimEditor editor = trimEditorMap.get(player);
        final ItemStack item = ev.getItem();

        if (editor == null
            || item == null
            || (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        final Block clickedBlock = ev.getClickedBlock();

        if (clickedBlock != null) {
            MMULogger.error(player, "Click away from blocks!");
            return;
        }

        final PlayerInventory inventory = player.getInventory();
        final ItemStack currentItem = editor.getCurrentItem();

        inventory.setItemInMainHand(currentItem);
        editor.setCurrentItem(item);

        ev.setCancelled(true);
    }

    @EventHandler()
    public void handleQuit(PlayerQuitEvent ev) {
        final Player player = ev.getPlayer();
        final TrimEditor editor = trimEditorMap.remove(player);

        if (editor == null) {
            return;
        }

        final CachedTrimData data = editor.remove();

        MMULogger.broadcastAdmins("%s left while editing a trim!", player.getName());
        MMULogger.broadcastAdmins("Here's their trim code: %s.", data.getHexId());
    }

    @EventHandler()
    public void handlePlayerToggleFlightEvent(PlayerToggleFlightEvent ev) {
        if (isEditing(ev.getPlayer())) {
            ev.setCancelled(true);
        }
    }

    public void enterEditor(@NotNull Player player) {
        if (player.getGameMode() != GameMode.CREATIVE) {
            MMULogger.error(player, "You must be in creative mode to use this!");
            return;
        }

        final TrimEditor oldEditor = trimEditorMap.get(player);

        if (oldEditor != null) {
            MMULogger.error(player, "You are already editing a trim!");
            MMULogger.error(player, "%s to leave the editor.", "&lSNEAK");
            return;
        }

        trimEditorMap.put(player, new TrimEditor(player));
    }

    public void exitEditor(@NotNull Player player) {
        trimEditorMap.remove(player);
    }

    @Nullable
    public CachedTrimData getData(int id) {
        return this.cachedTrims.get(id);
    }

    @NotNull
    protected CachedTrimData cache(@NotNull ItemStack[] itemStack) {
        final int nextId = cachedTrims.size();
        final CachedTrimData cachedTrimData = new CachedTrimData(nextId, itemStack);

        cachedTrims.add(cachedTrimData);
        return cachedTrimData;
    }

    private boolean isEditing(@NotNull Player player) {
        return trimEditorMap.containsKey(player);
    }
}
