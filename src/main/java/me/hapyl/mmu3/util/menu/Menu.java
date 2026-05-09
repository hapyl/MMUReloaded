package me.hapyl.mmu3.util.menu;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Map;

public abstract class Menu {
    
    public final static ItemStack ITEM_PANEL = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)
            .setName(Component.empty())
            .icon();
    
    public final static ItemStack ITEM_CLOSE_MENU = new ItemBuilder(Material.BARRIER)
            .setName(Component.text("Close Menu", NamedTextColor.RED))
            .build();
    
    private final Player player;
    private final Inventory inventory;
    private final Size size;
    
    private final Map<Integer, MenuAction> actions;
    
    public Menu(@NotNull Player player, @NotNull Component name, @NotNull Size size) {
        this.player = player;
        this.inventory = Bukkit.createInventory(null, size.rows * 9, name);
        this.size = size;
        this.actions = Maps.newHashMap();
    }
    
    @ApiStatus.OverrideOnly
    public abstract void updateMenu();
    
    @NotNull
    public Player getPlayer() {
        return player;
    }
    
    @NotNull
    public Size getSize() {
        return size;
    }
    
    public void setAction(int slot, @NotNull MenuAction action) {
        setItem(slot, null, action);
    }
    
    public void setItem(int slot, @NotNull ItemStack itemStack) {
        setItem(slot, itemStack, null);
    }
    
    public void setItem(int slot, @Nullable ItemStack itemStack, @Nullable MenuAction menuAction) {
        final int inventorySize = inventory.getSize();
        
        if (slot < 0 || slot > inventorySize - 1) {
            throw new IndexOutOfBoundsException("Slot %s cannot be negative nor greater than %s!".formatted(slot, inventorySize));
        }
        
        if (itemStack != null) {
            inventory.setItem(slot, itemStack);
        }
        
        if (menuAction != null) {
            actions.put(slot, menuAction);
        }
    }
    
    public void setPanelItem(@Range(from = 0, to = 8) int panelSlot, @NotNull ItemStack itemStack, @Nullable MenuAction menuAction) {
        final int slot = inventory.getSize() - (9 - panelSlot);
        
        setItem(slot, itemStack, menuAction);
    }
    
    public void setPanelItem(@Range(from = 0, to = 8) int panelSlot, @NotNull ItemStack itemStack) {
        setPanelItem(panelSlot, itemStack, null);
    }
    
    @OverridingMethodsMustInvokeSuper
    public void onClick(@NotNull Player player, @NotNull ClickType clickType, int slot) {
        final MenuAction menuAction = actions.get(slot);
        
        if (menuAction != null) {
            menuAction.use(this, player, clickType, slot);
        }
    }
    
    public void openMenu() {
        MenuHandler.PLAYER_MENU_MAP.put(player, this);
        
        // Clear item stacks and actions
        inventory.clear();
        actions.clear();
        
        // Call menu update
        this.updateMenu0();
        
        player.openInventory(inventory);
    }
    
    public void closeMenu() {
        player.closeInventory();
    }
    
    public final boolean compareInventory(@NotNull Inventory inventory) {
        // We must compare via equals, because I'm pretty sure inventories in events are always views, so identity check won't work
        return this.inventory.equals(inventory);
    }
    
    protected final void updateMenu0() {
        // Always set the panel at the bottom
        final int inventorySize = inventory.getSize();
        
        for (int slot = inventorySize - 8; slot <= inventorySize - 1; slot++) {
            setItem(slot, ITEM_PANEL);
        }
        
        // Set close menu button
        setItem(inventorySize - 4, ITEM_CLOSE_MENU, MenuAction.of(player -> this.closeMenu()));
        
        this.updateMenu();
    }
    
}