package me.hapyl.mmu3.command;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.block.BlockReplacer;
import me.hapyl.mmu3.message.Message;
import me.hapyl.eterna.module.command.SimplePlayerAdminCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BlockReplacerCommand extends SimplePlayerAdminCommand {
    // todo
    public BlockReplacerCommand(String name) {
        super(name);

        setDescription("Allows to bind block state to a block.");
        setUsage("/blockreplacer [Material]");
        setAliases("br");
    }

    @Override
    protected void execute(Player player, String[] args) {
        final ItemStack item = player.getInventory().getItemInMainHand();
        final BlockReplacer replacer = Main.getRegistry().replacer;
        final Material type = item.getType();

        if (type.isAir()) {
            Message.error(player, "You must be holding an item!");
            return;
        }

        if (!type.isBlock()) {
            Message.error(player, "Material must be a block!");
            return;
        }

        if (args.length == 0) {
            if (!replacer.hasReplacer(player, type)) {
                Message.error(player, "You don't have a replacer for this block!");
                return;
            }

            replacer.remove(player, type);
            Message.success(player, "Removed replacer for %s!", type.name());
        }
        else {
            final Material material = Material.matchMaterial(args[0]);

            if (material == null) {
                Message.error(player, "Invalid material!");
                return;
            }

            if (!material.isBlock()) {
                Message.error(player, "Material must be a block!");
                return;
            }

            replacer.set(player, type, material);
            Message.success(player, "Added replacer for %s!", type.name());
        }


    }
}
