package me.hapyl.mmu3.command;

import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;

public class NumericIdCommand extends SimplePlayerAdminCommand {

    // TODO: 021, Oct 21, 2022 fixme8
    public NumericIdCommand(String name) {
        super(name);
        setDescription("Allows to show numeric ID of held item.");
    }

    @Override
    protected void execute(Player player, String[] args) {
        ItemStack item;

        if (args.length == 0) {
            item = player.getInventory().getItemInMainHand();

            if (item.getType().isAir()) {
                final RayTraceResult trace = player.rayTraceBlocks(10);
                if (trace == null) {
                    item = null;
                }
                else {
                    final Block hitBlock = trace.getHitBlock();
                    item = hitBlock == null ? null : new ItemStack(hitBlock.getType());
                }
            }
        }
        else {
            item = new ItemStack(Validate.getEnumValue(Material.class, args[0], Material.AIR));
        }

        if (item == null || item.getType().isAir()) {
            Message.error(player, "Invalid item.");
            return;
        }

        final int itemId = getNumericId(item);
        if (itemId == -1) {
            Message.error(player, "Could not fetch item ID.");
            return;
        }
        Message.info(player, "%s numeric ID is %s.", Chat.capitalize(item.getType()), itemId);
    }

    private int getNumericId(ItemStack item) {
        final Material legacy = Validate.getEnumValue(Material.class, "LEGACY_" + item.getType().name());
        return legacy == null ? -1 : legacy.getId();
    }
}
