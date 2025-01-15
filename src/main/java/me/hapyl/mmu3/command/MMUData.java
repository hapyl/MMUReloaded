package me.hapyl.mmu3.command;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.nbt.NbtWrapper;
import me.hapyl.eterna.module.util.ArgumentList;
import me.hapyl.mmu3.message.Message;
import net.minecraft.nbt.NBTBase;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MMUData extends MMUCommand {

    public MMUData(@Nonnull String name) {
        super(name);
    }

    @Nonnull
    @Override
    protected String description() {
        return "Allows reading/modifying target entity NBT data.";
    }

    @Nullable
    @Override
    protected String usage() {
        return "/mdata [key] [value]";
    }

    @Override
    protected void execute(@Nonnull Player player, @Nonnull ArgumentList args) {
        final Entity targetEntity = player.getTargetEntity(10, true);

        if (targetEntity == null) {
            Message.error(player, "You aren't targeting anything!");
            return;
        }

        final NbtWrapper nbt = NbtWrapper.of(targetEntity);

        // mdata
        // mdata [key]
        // mdata [key] [new_value]

        // No arguments means read all data
        if (args.length == 0) {
            Message.success(player, "%s's data:".formatted(Chat.capitalize(targetEntity.getType())));
            Message.info(player, nbt.toString().replace("%", "%%"));
            return;
        }
        else {
            final String key = args.getString(0);

            final NBTBase nbtBase = nbt.get(key);
        }
    }

}
