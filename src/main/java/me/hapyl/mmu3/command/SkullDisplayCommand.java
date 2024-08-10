package me.hapyl.mmu3.command;

import com.destroystokyo.paper.profile.PlayerProfile;
import me.hapyl.eterna.module.command.SimplePlayerAdminCommand;
import me.hapyl.eterna.module.entity.Entities;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.mmu3.message.Message;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Skull;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;

import java.util.Optional;

public class SkullDisplayCommand extends SimplePlayerAdminCommand {

    // TODO (Sat, Aug 10 2024 @xanyjl): Maybe bind to F or something but works for now

    public SkullDisplayCommand(String name) {
        super(name);

        setDescription("Allows spawning or changing head textures as item displays to later move via Axiom.");
        addCompleterValues(0, "spawn", "replace");
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

            default -> {
                Message.error(player, "Invalid argument!");
            }
        }
    }

    private void replaceDisplay(Player player, Display display, SkullMeta skullItem) {
        if (display instanceof ItemDisplay itemDisplay) {
            itemDisplay.setItemStack(asItem(skullItem));
        }
        else if (display instanceof BlockDisplay blockDisplay) {
            blockDisplay.setBlock(asBlockData(skullItem));
        }

        Message.success(player, "Replaced!");
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

            final Optional<Display> display = world.getNearbyEntitiesByType(Display.class, location, 0.5d, 0.5d)
                    .stream()
                    .min((o1, o2) -> {
                        final double d1 = o1.getLocation().distance(location);
                        final double d2 = o2.getLocation().distance(location);

                        return Double.compare(d2, d1);
                    });

            if (display.isPresent()) {
                return display.get();
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
