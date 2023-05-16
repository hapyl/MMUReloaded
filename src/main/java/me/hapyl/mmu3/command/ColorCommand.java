package me.hapyl.mmu3.command;

import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.awt.*;

public class ColorCommand extends SimplePlayerAdminCommand {
    public ColorCommand(String name) {
        super(name);

        setDescription("Allows to change color for leather items.");
        setUsage("color [#color]");
    }

    @Override
    protected void execute(Player player, String[] args) {
        final ItemStack item = player.getInventory().getItemInMainHand();

        if (!(item.getItemMeta() instanceof LeatherArmorMeta leatherMeta)) {
            Message.error(player, "This item cannot be colored!");
            return;
        }

        // Remove color
        if (args.length == 0) {
            leatherMeta.setColor(null);
            item.setItemMeta(leatherMeta);

            Message.success(player, "Cleared color.");
            Message.sound(player, Sound.ITEM_BUCKET_EMPTY, 0.0f);
            return;
        }

        final Color color = colorFromString(args[0]);
        leatherMeta.setColor(org.bukkit.Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue()));
        item.setItemMeta(leatherMeta);

        Message.success(player, "Changed color to %s.", args[0]);
    }

    private Color colorFromString(String string) {
        // Add # if not present
        if (!string.startsWith("#")) {
            string = "#" + string;
        }
        // Trim to only have one # (In case if pasted with #)
        else {
            string = string.substring(string.lastIndexOf("#"));
        }

        // Trim to min of 7
        string = string.substring(0, Math.min(string.length(), 7));
        return java.awt.Color.decode(string);
    }
}
