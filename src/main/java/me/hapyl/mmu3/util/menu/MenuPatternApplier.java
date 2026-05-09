package me.hapyl.mmu3.util.menu;

import com.google.common.collect.Lists;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MenuPatternApplier {
    
    private final Menu menu;
    private final MenuPattern menuPattern;
    private final int startRow;
    
    private final List<ItemStackMenuAction> actions;
    
    public MenuPatternApplier(@NotNull Menu menu, @NotNull MenuPattern menuPattern, int startRow) {
        this.menu = menu;
        this.menuPattern = menuPattern;
        this.startRow = startRow;
        this.actions = Lists.newArrayList();
    }
    
    public void add(@NotNull ItemStack itemStack, @NotNull MenuAction menuAction) {
        actions.add(new ItemStackMenuAction.ItemStackMenuActionImpl(itemStack, menuAction));
    }
    
    public void add(@NotNull ItemStack itemStack) {
        actions.add(new ItemStackMenuAction.ItemStackMenuActionImpl(itemStack, null));
    }
    
    public void apply() {
        final List<List<ItemStackMenuAction>> sublists = Lists.newArrayList();
        final List<ItemStackMenuAction> actions = Lists.newArrayList(this.actions);
        
        final int width = menuPattern.maxWidth();
        final int size = actions.size();
        
        // Split into sublist that fits the max width pattern
        for (int i = 0; i < size; i += width) {
            sublists.add(actions.subList(i, Math.min(i + width, size)));
        }
        
        // Apply pattern on the sublists
        for (List<ItemStackMenuAction> sublist : sublists) {
            final byte[] pattern = menuPattern.patternFor(sublist.size());
            
            int index = 0;
            
            for (int i = 0; i < pattern.length; i++) {
                final byte value = pattern[i];
                
                if (value != 0) {
                    final int slot = i + startRow * MenuPattern.CONTAINER_LENGTH;
                    final ItemStackMenuAction itemStackMenuAction = sublist.get(index++);
                    
                    menu.setItem(slot, itemStackMenuAction.itemStack(), itemStackMenuAction.menuAction());
                }
            }
        }
    }
    
    public interface ItemStackMenuAction {
        
        @NotNull
        ItemStack itemStack();
        
        @Nullable
        MenuAction menuAction();
        
        class ItemStackMenuActionImpl implements ItemStackMenuAction {
            private final ItemStack itemStack;
            private final MenuAction menuAction;
            
            ItemStackMenuActionImpl(@NotNull ItemStack itemStack, @Nullable MenuAction menuAction) {
                this.itemStack = itemStack;
                this.menuAction = menuAction;
            }
            
            @NotNull
            @Override
            public ItemStack itemStack() {
                return itemStack;
            }
            
            @Nullable
            @Override
            public MenuAction menuAction() {
                return menuAction;
            }
        }
        
    }
    
}