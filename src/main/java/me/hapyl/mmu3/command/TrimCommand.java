package me.hapyl.mmu3.command;

import me.hapyl.mmu3.feature.trim.EnumTrimMaterial;
import me.hapyl.mmu3.feature.trim.EnumTrimPattern;
import me.hapyl.mmu3.feature.trim.TrimGUI;
import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;

public class TrimCommand extends SimplePlayerAdminCommand {

    public TrimCommand(String name) {
        super(name);

        addCompleterValues(1, EnumTrimPattern.values());
        addCompleterValues(2, EnumTrimMaterial.values());
    }

    @Override
    protected void execute(Player player, String[] args) {
        if (args.length == 0) {
            new TrimGUI(player);
            return;
        }

        final PlayerInventory inventory = player.getInventory();
        final ItemStack item = inventory.getItemInMainHand();
        final ItemMeta meta = item.getItemMeta();

        if (item.getType().isAir()) {
            Message.error(player, "You must hold an item!");
            return;
        }

        if (!(meta instanceof ArmorMeta armorMeta)) {
            Message.error(player, "This item cannot be trimmed!");
            return;
        }


        final EnumTrimPattern pattern = getArgument(args, 0).toEnum(EnumTrimPattern.class);
        final EnumTrimMaterial material = getArgument(args, 1).toEnum(EnumTrimMaterial.class);

        if (pattern == null) {
            Message.error(player, "Invalid pattern!");
            return;
        }

        if (material == null) {
            Message.error(player, "Invalid material!");
            return;
        }

        armorMeta.setTrim(new ArmorTrim(material.bukkit, pattern.bukkit));
        item.setItemMeta(armorMeta);

        Message.success(player, "Applied %s with %s to the item!", pattern.getName(), material.getName());
    }

}
