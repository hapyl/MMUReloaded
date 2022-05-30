package me.hapyl.mmu3.command;

import me.hapyl.mmu3.Message;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class HatCommand extends SimplePlayerAdminCommand {
    public HatCommand(String name) {
        super(name);
        setDescription("Allows to put held item on players' head.");
    }

    @Override
    protected void execute(Player player, String[] strings) {
        final PlayerInventory inventory = player.getInventory();
        final ItemStack heldItem = inventory.getItemInMainHand();
        if (heldItem.getType().isAir()) {
            Message.error(player, "You aren't holding any item!");
            return;
        }

        final ItemStack helmet = inventory.getHelmet();
        inventory.setHelmet(heldItem);

        Message.success(player, "Put %s on your head!", Chat.capitalize(heldItem.getType()));
        Message.sound(player, Sound.ENTITY_CHICKEN_EGG, 1.0f);

        if (helmet != null) {
            inventory.setItemInMainHand(helmet);
        }
        else {
            heldItem.setAmount(0);
        }
    }
}
