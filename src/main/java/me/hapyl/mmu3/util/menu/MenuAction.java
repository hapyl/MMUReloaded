package me.hapyl.mmu3.util.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

public interface MenuAction {

    void use(@NotNull Menu menu, @NotNull Player player, @NotNull ClickType clickType, int clickedSlot);

    @NotNull
    static MenuAction of(@NotNull Action action) {
        return (menu, player, clickType, clickedSlot) -> action.use(player);
    }

    @NotNull
    static MenuAction of(@NotNull MenuAction menuAction) {
        return menuAction;
    }

    @NotNull
    static Builder builder() {
        return new Builder();
    }

    class Builder implements MenuAction {

        private final MenuActionImpl impl;

        private Builder() {
            this.impl = new MenuActionImpl();
        }

        @Override
        public void use(@NotNull Menu menu, @NotNull Player player, @NotNull ClickType clickType, int clickedSlot) {
            impl.use(menu, player, clickType, clickedSlot);
        }

        @NotNull
        public Builder left(@NotNull Action action) {
            impl.actions.put(ClickType.LEFT, action);
            return this;
        }

        @NotNull
        public Builder right(@NotNull Action action) {
            impl.actions.put(ClickType.RIGHT, action);
            return this;
        }

        @NotNull
        public Builder shiftLeft(@NotNull Action action) {
            impl.actions.put(ClickType.SHIFT_LEFT, action);
            return this;
        }

        @NotNull
        public Builder shiftRight(@NotNull Action action) {
            impl.actions.put(ClickType.SHIFT_RIGHT, action);
            return this;
        }
        
        @NotNull
        public Builder middle(@NotNull Action action) {
            impl.actions.put(ClickType.MIDDLE, action);
            return this;
        }
    }

}
