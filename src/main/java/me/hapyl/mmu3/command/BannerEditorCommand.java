package me.hapyl.mmu3.command;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.banner.BannerEditorGUI;
import me.hapyl.mmu3.feature.banner.BannerSerializer;
import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BannerEditorCommand extends SimplePlayerAdminCommand {
    public BannerEditorCommand(String name) {
        super(name);

        setAliases("be", "banner");
    }

    @Override
    protected void execute(Player player, String[] args) {
        // bannerEditor [clear, code]

        if (args.length == 0) {
            new BannerEditorGUI(player);
            return;
        }

        final String argument = getArgument(args, 0).toString();

        if (argument.equalsIgnoreCase("clear")) {
            Main.getRegistry().bannerEditor.remove(player);
            return;
        }

        final ItemStack banner = BannerSerializer.deserializeSafe(argument);

        if (banner == null) {
            Message.error(player, "Invalid string!");
            return;
        }

        player.getInventory().addItem(banner);
        Message.success(player, "Gave you a banner!");
    }
}
