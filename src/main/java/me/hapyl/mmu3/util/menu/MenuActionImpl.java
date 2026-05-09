package me.hapyl.mmu3.util.menu;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

public class MenuActionImpl implements MenuAction {

    final EnumMap<ClickType, Action> actions;

    MenuActionImpl() {
        this.actions = Maps.newEnumMap(ClickType.class);
    }

    @Override
    public void use(@NotNull Menu menu, @NotNull Player player, @NotNull ClickType clickType, int clickedSlot) {
        final Action menuAction = actions.get(clickType);

        if (menuAction != null) {
            menuAction.use(player);
        }
    }
}
