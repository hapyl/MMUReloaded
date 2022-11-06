package me.hapyl.mmu3.command;

import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.utils.nbt.NBTData;
import me.hapyl.mmu3.utils.nbt.NBTHolder;
import me.hapyl.mmu3.utils.nbt.NBTTagVisitor;
import me.hapyl.spigotutils.module.chat.LazyEvent;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import net.minecraft.nbt.NBTBase;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NBTCommand extends SimplePlayerAdminCommand {
    public NBTCommand(String name) {
        super(name);
    }

    // nbt [optional]
    @Override
    protected void execute(Player player, String[] args) {
        final ItemStack item = player.getInventory().getItemInMainHand();
        //        final RayTraceResult trace = player.rayTraceBlocks(25);

        if (item.getType().isAir()) {
            Message.error(player, "Not holding item.");
            return;
        }

        final NBTHolder holder = new NBTHolder(item);

        // TODO
        //        if (trace != null && trace.getHitEntity() != null) {
        //            holder = new NBTHolder(trace.getHitEntity());
        //        }
        //        else if (trace != null && trace.getHitBlock() != null) {
        //            holder = new NBTHolder(trace.getHitBlock());
        //        }
        //        else if (!item.getType().isAir()) {
        //            holder = new NBTHolder(item);
        //        }

        if (!holder.hasData()) {
            Message.error(player, "Unable to fetch NBT.");
            return;
        }

        Message.success(player, "Display NBT for %s:", holder.getName());

        // Search
        if (args.length == 1) {
            final NBTData data = holder.getNbt(args[0]);

            if (data == null) {
                Message.error(player, "Could not find nbt value '%s'.", args[0]);
                return;
            }

            formatDisplay(player, data.getKey(), data.getValue());
            return;
        }

        // Display all
        holder.getMappedValues().forEach((k, v) -> {
            formatDisplay(player, k, v);
        });

    }

    private void formatDisplay(Player player, String key, NBTBase nbt) {
        Message.clickHover(
                player,
                LazyEvent.copyToClipboard(key + ": " + nbt),
                LazyEvent.showText("&eClick to copy '&6%s&e'.", key),
                color(key, nbt)
        );
    }

    private String color(String key, NBTBase nbt) {
        return " &b" + key + "&f: " + new NBTTagVisitor().toString(nbt);
    }

}
