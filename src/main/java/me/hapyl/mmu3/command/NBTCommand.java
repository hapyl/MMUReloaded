package me.hapyl.mmu3.command;

import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.utils.nbt.NBTData;
import me.hapyl.mmu3.utils.nbt.NBTHolder;
import me.hapyl.mmu3.utils.nbt.NBTTagVisitor;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.chat.LazyEvent;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.nbt.nms.NBTNative;
import net.minecraft.nbt.NBTBase;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class NBTCommand extends SimplePlayerAdminCommand {
    public NBTCommand(String name) {
        super(name);
        setDescription("Allows to read and write NBT to items, blocks or entities.");
        setUsage("nbt [Path],({Nbt})");
    }

    // nbt [optional]
    // nbt {}
    @Override
    protected void execute(Player player, String[] args) {
        final PlayerInventory playerInventory = player.getInventory();
        final ItemStack item = playerInventory.getItemInMainHand();
        //        final RayTraceResult trace = player.rayTraceBlocks(25);

        if (item.getType().isAir()) {
            Message.error(player, "Not holding item.");
            return;
        }

        // Setting the NBT
        if (args.length >= 1 && args[0].startsWith("{")) {
            try {
                playerInventory.setItemInMainHand(NBTNative.setNbt(item, Chat.arrayToString(args, 0)));
                Message.success(player, "Modified held item NBT.");
            } catch (Exception e) {
                Message.error(player, "Unable to modify NBT! " + e.getMessage());
            }
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
            Message.error(player, "Item doesn't contains any NBT data.");
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
