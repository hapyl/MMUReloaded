package me.hapyl.mmu3.command;

import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.outcast.hypixel.slayer.SlayerData;
import me.hapyl.mmu3.outcast.hypixel.slayer.SlayerQuest;
import me.hapyl.mmu3.outcast.hypixel.slayer.SlayerType;
import me.hapyl.mmu3.outcast.hypixel.slayer.boss.SlayerBoss;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.util.RomanNumber;
import me.hapyl.spigotutils.module.util.Validate;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SlayerBossCommand extends SimplePlayerAdminCommand {
    public SlayerBossCommand(String name) {
        super(name);
        setDescription("Spawns a slayer boss.");
        setAliases("sb");
    }

    @Override
    protected void execute(Player player, String[] args) {
        // slayerboss (Type) (Tier)
        if (args.length != 2) {
            Message.NOT_ENOUGH_ARGUMENTS_EXPECTED.send(player, 2);
            return;
        }

        final SlayerType slayerType = Validate.getEnumValue(SlayerType.class, args[0]);
        final int tier = Validate.getInt(args[1]);

        if (slayerType == null) {
            Message.error(player, "Slayer boss doesn't exist!");
            return;
        }

        final SlayerQuest quest = slayerType.getQuests().getTier(tier);
        final SlayerData data = slayerType.getData();

        if ((tier < 0 || tier > 5) || quest == null) {
            Message.error(player, "%s doesn't have tier %s boss!", data.getName(), tier);
            return;
        }

        final SlayerBoss boss = quest.getBoss();

        if (boss == null) {
            Message.error(player, "%s doesn't have a boss!", data.getName());
            return;
        }

        boss.spawnBoss(player.getLocation(), null);
        Message.info(player, "Spawned %s %s!", data.getName(), RomanNumber.toRoman(tier));
    }

    @Override
    protected List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return completerSort(SlayerType.values(), args);
        }

        return null;
    }
}
