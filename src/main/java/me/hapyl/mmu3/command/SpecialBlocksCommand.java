package me.hapyl.mmu3.command;

import kz.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.Message;
import me.hapyl.mmu3.feature.specialblocks.SpecialBlock;
import me.hapyl.mmu3.feature.specialblocks.SpecialBlocksGUI;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SpecialBlocksCommand extends SimplePlayerAdminCommand {
    public SpecialBlocksCommand(String name) {
        super(name);
        setAliases("special", "blocks");
        setDescription("Opens special blocks menu.");
    }

    @Override
    protected void execute(Player player, String[] args) {
        if (args.length == 0) {
            new SpecialBlocksGUI(player);
            return;
        }

        final String arg = args[0];
        final String id = arg.contains("sb.") ? arg : "sb." + arg;

        final SpecialBlock specialBlock = Main.getSpecialBlocks().byId(id);
        if (specialBlock == null) {
            Message.error(player, "Could not find special block with ID %s.", id);
            Message.sound(player, Sound.ENTITY_VILLAGER_NO);
            return;
        }

        specialBlock.giveItem(player);
    }

    @Override
    protected List<String> tabComplete(CommandSender sender, String[] args) {
        return completerSort(Main.getSpecialBlocks().getIdsNoSb(), args);
    }
}
