package me.hapyl.mmu3.util.menu;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public final class MenuHandler implements Listener {

    static final Map<Player, Menu> PLAYER_MENU_MAP;

    static {
        PLAYER_MENU_MAP = Maps.newHashMap();
    }

    @EventHandler
    public void handleInventoryClickEvent(InventoryClickEvent ev) {
        if (!(ev.getWhoClicked() instanceof Player player)) {
            return;
        }

        final ClickType clickType = ev.getClick();
        final int slot = ev.getRawSlot();

        fetchMenu(player, ev.getInventory()).ifPresent(menu -> {
            menu.onClick(player, clickType, slot);
        });
    }

    @EventHandler
    public void handleInventoryCloseEvent(InventoryCloseEvent ev) {
        if (!(ev.getPlayer() instanceof Player player)) {
            return;
        }

        final Menu menu = PLAYER_MENU_MAP.get(player);

        if (menu == null || !menu.compareInventory(ev.getInventory())) {
            return;
        }

        PLAYER_MENU_MAP.remove(player);
    }

    @NotNull
    private static Optional<Menu> fetchMenu(@NotNull Player player, @NotNull Inventory inventory) {
        final Menu menu = PLAYER_MENU_MAP.get(player);

        return menu != null && menu.compareInventory(inventory)
                ? Optional.of(menu)
                : Optional.empty();
    }

}
