package me.hapyl.mmu3.command;

import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.outcast.hypixel.slayer.SlayerType;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.math.Numbers;
import me.hapyl.spigotutils.module.util.Validate;
import org.bukkit.entity.Player;

public class SlayerDropCommand extends SimplePlayerAdminCommand {
    public SlayerDropCommand(String name) {
        super(name);
        setDescription("Allows to drop slayers items for admins.");
    }

    @Override
    protected void execute(Player player, String[] args) {
        // slayerdrop (Type) (Tier) [luck]
        if (args.length >= 2) {

            final SlayerType slayer = Validate.getEnumValue(SlayerType.class, args[0]);
            final int tier = Numbers.clamp(Numbers.getInt(args[1], 1), 1, 5);

            return;
        }

        Message.NOT_ENOUGH_ARGUMENTS_EXPECTED_AT_LEAST.send(player, 2);
    }
}
