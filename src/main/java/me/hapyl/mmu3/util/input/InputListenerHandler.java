package me.hapyl.mmu3.util.input;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;
import me.hapyl.mmu3.message.Message;
import org.bukkit.Input;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.function.Predicate;

@SuppressWarnings("UnstableApiUsage")
public final class InputListenerHandler extends Feature implements Listener {

    private static final Map<InputType, Predicate<Input>> inputPredicates = Map.of(
            InputType.W, Input::isForward,
            InputType.S, Input::isBackward,
            InputType.A, Input::isLeft,
            InputType.D, Input::isRight,
            InputType.SPACE, Input::isJump,
            InputType.SNEAK, Input::isSneak,
            InputType.SPRINT, Input::isSprint
    );

    private final Map<Player, InputData> listeners;

    public InputListenerHandler(@Nonnull Main main) {
        super(main);

        this.listeners = Maps.newHashMap();
    }

    @EventHandler
    public void handlePlayerInputEvent(PlayerInputEvent ev) {
        final Player player = ev.getPlayer();
        final InputData data = listeners.get(player);
        final Input input = ev.getInput();

        if (data == null) {
            return;
        }

        inputPredicates.forEach((type, predicate) -> {
            if (predicate.test(input)) {
                data.listener.listen(player, type);
            }
        });
    }

    @EventHandler
    public void handlePlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent ev) {
        if (workListener(ev.getPlayer(), InputType.F)) {
            ev.setCancelled(true);
        }
    }

    @EventHandler
    public void handleEntityInteractEvent(PlayerInteractEvent ev) {
        final Action action = ev.getAction();
        final Player player = ev.getPlayer();
        final InputData data = listeners.get(player);

        if (data == null) {
            return;
        }

        final InputType type = action.isRightClick() ? InputType.RIGHT : action.isLeftClick() ? InputType.LEFT : null;

        if (type != null) {
            data.listener.listen(player, type);
        }

        ev.setCancelled(true);
    }

    @EventHandler
    public void handleEntityInteractAtEntityEvent(PlayerInteractEntityEvent ev) {
        if (workListener(ev.getPlayer(), InputType.RIGHT)) {
            ev.setCancelled(true);
        }
    }

    @EventHandler
    public void handlePlayerArmorStandManipulateEvent(PlayerArmorStandManipulateEvent ev) {
        if (workListener(ev.getPlayer(), InputType.RIGHT)) {
            ev.setCancelled(true);
        }
    }

    @EventHandler
    public void handleEntityDamageByEntityEvent(EntityDamageByEntityEvent ev) {
        if (!(ev.getDamager() instanceof Player player)) {
            return;
        }

        if (workListener(player, InputType.LEFT)) {
            ev.setCancelled(true);
        }
    }

    @EventHandler
    public void handlePlayerToggleFlightEvent(PlayerToggleFlightEvent ev) {
        if (listeners.containsKey(ev.getPlayer())) {
            ev.setCancelled(true);
        }
    }

    @EventHandler
    public void handlePlayerQuitEvent(PlayerQuitEvent ev) {
        unlisten(ev.getPlayer());
    }

    void listen(@Nonnull Player player, @Nonnull InputListener listener) {
        unlisten(player);

        // Show usage
        Message.success(player, "&2You've entered &6%s&2!".formatted(listener.name()));

        listener.showUsage(player);
        listener.listen(player, InputType.START_LISTENING);

        listeners.put(player, new InputData(player, listener));
    }

    void unlisten(@Nonnull Player player) {
        final InputData data = listeners.remove(player);

        if (data != null) {
            // Show that we left it
            Message.success(player, "&2You've left &6%s&2!".formatted(data.listener.name()));

            data.listener.listen(player, InputType.STOP_LISTENING);
            data.dispose();
        }
    }

    private boolean workListener(Player player, InputType type) {
        final InputData data = listeners.get(player);

        if (data != null) {
            data.listener.listen(player, type);
            return true;
        }

        return false;
    }
}
