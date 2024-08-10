package me.hapyl.mmu3.command;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.common.collect.Maps;
import me.hapyl.eterna.module.chat.LazyEvent;
import me.hapyl.eterna.module.command.SimplePlayerAdminCommand;
import me.hapyl.eterna.module.entity.Entities;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.util.Runnables;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.utils.Revertible;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Skull;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SkullDisplayCommand extends SimplePlayerAdminCommand {

    // TODO (Sat, Aug 10 2024 @xanyjl): Maybe bind to F or something but works for now

    private final Map<UUID, Revertible> latestChange;

    public SkullDisplayCommand(String name) {
        super(name);

        setDescription("Allows spawning or changing head textures as item displays to later move via Axiom.");
        addCompleterValues(0, "spawn", "replace");

        this.latestChange = Maps.newHashMap();
    }

    @Override
    protected void execute(Player player, String[] args) {
        final ItemStack handItem = player.getInventory().getItemInMainHand();
        final String argument = getArgument(args, 0).toString();

        if (handItem.isEmpty()) {
            Message.error(player, "You must be holding an item to spawn a display!");
            return;
        }

        if (!(handItem.getItemMeta() instanceof SkullMeta skullMeta)) {
            Message.error(player, "You must be holding a player skull to spawn a display!");
            return;
        }
        if (args.length == 0) {
            final Display targetDisplay = getTargetDisplay(player);

            if (targetDisplay == null) {
                spawnSkull(player, skullMeta);
            }
            else {
                replaceDisplay(player, targetDisplay, skullMeta);
            }
            return;
        }

        switch (argument.toLowerCase()) {
            case "spawn" -> {
                spawnSkull(player, skullMeta);
            }

            case "replace" -> {
                final Display display = getTargetDisplay(player);

                if (display == null) {
                    Message.error(player, "Not targeting any display!");
                    return;
                }

                if (display instanceof TextDisplay) {
                    Message.error(player, "Cannot set item for text display!");
                    return;
                }

                replaceDisplay(player, display, skullMeta);
            }

            case "undo" -> {
                final Revertible revertible = latestChange.remove(player.getUniqueId());

                if (revertible == null) {
                    Message.error(player, "Nothing to undo!");
                    return;
                }

                revertible.revert();
                Message.success(player, "Reverted latest change!");
            }

            default -> {
                Message.error(player, "Invalid argument!");
            }
        }
    }

    private void replaceDisplay(Player player, Display display, SkullMeta skullItem) {
        if (display instanceof ItemDisplay itemDisplay) {
            final ItemStack revert = itemDisplay.getItemStack();
            latestChange.put(player.getUniqueId(), () -> itemDisplay.setItemStack(revert));

            itemDisplay.setItemStack(asItem(skullItem));
        }
        else if (display instanceof BlockDisplay blockDisplay) {
            final BlockData revert = blockDisplay.getBlock();
            latestChange.put(player.getUniqueId(), () -> blockDisplay.setBlock(revert));

            blockDisplay.setBlock(asBlockData(skullItem));
        }

        Message.clickHover(player, LazyEvent.runCommand(getUsage().toLowerCase() + " undo"), LazyEvent.showText("&eClick to undo!"), "&aReplaced! &6&lUNDO");
    }

    private Display getTargetDisplay(Player player) {
        final Location location = player.getEyeLocation();
        final Vector direction = location.getDirection();
        final World world = location.getWorld();

        for (double d = 0; d < 10; d += 0.5) {
            final double x = direction.getX() * d;
            final double y = direction.getY() * d;
            final double z = direction.getZ() * d;

            location.add(x, y, z);

            final Optional<Display> optionalDisplay = world.getNearbyEntitiesByType(Display.class, location, 0.3d, 0.3d)
                    .stream()
                    .min((o1, o2) -> {
                        final double d1 = o1.getLocation().distance(location);
                        final double d2 = o2.getLocation().distance(location);

                        return Double.compare(d1, d2);
                    });

            if (optionalDisplay.isPresent()) {
                final Display display = optionalDisplay.get();

                display.setGlowColorOverride(Color.ORANGE);
                display.setGlowing(true);

                Runnables.runLater(() -> {
                    display.setGlowColorOverride(null);
                    display.setGlowing(false);
                }, 20);

                return display;
            }

            location.subtract(x, y, z);
        }

        return null;
    }

    private ItemStack asItem(SkullMeta skullMeta) {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .modifyMeta(SkullMeta.class, meta -> meta.setPlayerProfile(skullMeta.getPlayerProfile()))
                .toItemStack();
    }

    private BlockData asBlockData(SkullMeta skullMeta) {
        final BlockData blockData = Material.PLAYER_HEAD.createBlockData();
        final PlayerProfile profile = skullMeta.getPlayerProfile();

        if (profile == null) {
            return blockData;
        }

        if (blockData instanceof Skull skull) {
            skull.setPlayerProfile(profile);
        }

        return blockData;
    }

    private void spawnSkull(Player player, SkullMeta skullMeta) {
        final Location location = player.getLocation();
        location.add(0, 0.5, 0);
        location.setYaw(0.0f);
        location.setPitch(0.0f);

        Entities.ITEM_DISPLAY.spawn(location, self -> self.setItemStack(asItem(skullMeta)));
        Message.success(player, "Spawned!");
    }

}
