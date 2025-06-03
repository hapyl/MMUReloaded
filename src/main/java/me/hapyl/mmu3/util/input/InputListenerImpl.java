package me.hapyl.mmu3.util.input;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class InputListenerImpl implements InputListener {

    private final String name;
    private final Map<InputType, Action> actions;

    private InputListenerImpl(@Nonnull String name) {
        this.name = name;
        this.actions = Maps.newHashMap();
    }

    @Nonnull
    @Override
    public String name() {
        return name;
    }

    public InputListenerImpl at(@Nonnull InputType type, @Nonnull String description, @Nonnull Consumer<Player> action) {
        actions.put(
                type, new Action(type, description) {
                    @Override
                    public void execute(Player player) {
                        action.accept(player);
                    }
                }
        );
        return this;
    }

    @Override
    public void showUsage(@Nonnull Player player) {
        final Map<String, List<InputType>> grouped = Maps.newHashMap();

        actions.values().forEach(action -> grouped.computeIfAbsent(action.about(), s -> Lists.newArrayList()).add(action.type));

        grouped.forEach((usage, types) -> {
            InputListener.showUsage(player, usage, types.toArray(InputType[]::new));
        });
    }

    @Override
    public void listen(@Nonnull Player player, @Nonnull InputType input) {
        final Action action = actions.get(input);

        if (action != null) {
            action.execute(player);
        }
    }

    @Nonnull
    public static InputListenerImpl builder(@Nonnull String name) {
        return new InputListenerImpl(name);
    }

    private abstract static class Action {
        private final InputType type;
        private final String description;

        private Action(InputType type, String description) {
            this.type = type;
            this.description = description;
        }

        @Nonnull
        public InputType type() {
            return type;
        }

        @Nonnull
        public String about() {
            return description;
        }

        public abstract void execute(Player player);
    }
}
