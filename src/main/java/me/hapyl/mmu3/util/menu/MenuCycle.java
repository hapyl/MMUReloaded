package me.hapyl.mmu3.util.menu;

import me.hapyl.mmu3.MMULogger;
import me.hapyl.mmu3.util.ButtonComponents;
import me.hapyl.mmu3.util.ItemBuilder;
import me.hapyl.mmu3.util.ItemCreator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;

public abstract class MenuCycle<E extends Enum<E> & ComponentLike> implements ItemCreator {
    
    private static final Material DEFAULT_MATERIAL = Material.NAME_TAG;
    private static final Component ARROW_CHAR = Component.text("➥");
    
    private final Component name;
    private final Component description;
    
    private final E[] values;
    
    public MenuCycle(@NotNull Component name, @NotNull Component description, @NotNull Class<E> enumClass) {
        this.name = name;
        this.description = description;
        this.values = enumClass.getEnumConstants();
    }
    
    @NotNull
    public abstract E currentValue();
    
    public abstract void currentValue(@NotNull E e);
    
    @NotNull
    public ItemBuilder createBuilder(@NotNull E e) {
        return new ItemBuilder(DEFAULT_MATERIAL);
    }
    
    @NotNull
    @Override
    public ItemBuilder createBuilder() {
        final E currentValue = currentValue();
        final ItemBuilder builder = createBuilder(currentValue);
        
        builder.setName(name);
        builder.addLore();
        builder.addLore(description);
        builder.addLore();
        
        // Append the values
        for (E value : values) {
            if (currentValue.equals(value)) {
                builder.addLore(ARROW_CHAR.appendSpace().append(value.asComponent()));
            }
            else {
                builder.addLore(value.asComponent().color(NamedTextColor.DARK_GRAY));
            }
        }
        
        builder.addLore();
        builder.addLore(ButtonComponents.left("cycle"));
        builder.addLore(ButtonComponents.right("cycle backwards"));
        
        return builder;
    }
    
    @NotNull
    public MenuAction createMenuAction(@NotNull Menu menu) {
        final E currentValue = currentValue();
        
        return MenuAction.builder()
                         .left(createAction(menu, offsetValue(currentValue, 1)))
                         .right(createAction(menu, offsetValue(currentValue, -1)));
    }
    
    @NotNull
    private Action createAction(@NotNull Menu menu, @NotNull E value) {
        return player -> {
            currentValue(value);
            menu.openMenu();
            
            // Fx
            MMULogger.sound(player, Sound.UI_BUTTON_CLICK, 1.25f);
        };
    }
    
    @NotNull
    private E offsetValue(@NotNull E currentValue, int offset) {
        if (offset == 0) {
            return currentValue;
        }
        
        final int index = currentValue.ordinal();
        final int newIndex = Math.floorMod(index + offset, values.length);
        
        return values[newIndex];
    }
    
}