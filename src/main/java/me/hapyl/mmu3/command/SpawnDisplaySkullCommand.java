package me.hapyl.mmu3.command;

import me.hapyl.eterna.module.command.SimplePlayerAdminCommand;
import me.hapyl.eterna.module.entity.Entities;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.mmu3.message.Message;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SpawnDisplaySkullCommand extends SimplePlayerAdminCommand {
    public SpawnDisplaySkullCommand(String name) {
        super(name);

        setDescription("Allows spawning head textures as item displays to later move via Axiom.");
    }

    @Override
    protected void execute(Player player, String[] args) {
        final ItemStack handItem = player.getInventory().getItemInMainHand();

        if (handItem.isEmpty()) {
            Message.error(player, "You must be holding an item to spawn a display!");
            return;
        }

        if (!(handItem.getItemMeta() instanceof SkullMeta skullMeta)) {
            Message.error(player, "You must be holding a player skull to spawn a display!");
            return;
        }

        final Location location = player.getLocation();
        location.add(0, 0.5, 0);
        location.setYaw(0.0f);
        location.setPitch(0.0f);

        Entities.ITEM_DISPLAY.spawn(location, self -> {
            final ItemBuilder builder = new ItemBuilder(Material.PLAYER_HEAD);
            builder.modifyMeta(SkullMeta.class, meta -> {
                meta.setPlayerProfile(skullMeta.getPlayerProfile());
            });

            self.setItemStack(builder.toItemStack());
        });

        Message.success(player, "Spawned!");
    }
}
