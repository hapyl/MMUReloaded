package me.hapyl.mmu3.command;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.itemcreator.ItemCreator;
import me.hapyl.mmu3.feature.itemcreator.gui.ItemCreatorGUI;
import me.hapyl.mmu3.feature.itemcreator.gui.LoreSubGUI;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ItemCreatorCommand extends SimplePlayerAdminCommand {

    private final String[] validArguments = { "open", "new", "import", "addsmartlore", "setsmartlore", "setheadtexture", "setnbt" };

    public ItemCreatorCommand(String name) {
        super(name);
        setDescription("Opens item creator gui.");
        setAliases("ic", "createItem", "create");
    }

    @Override
    protected void execute(Player player, String[] args) {
        // ic (open, new, import, addsmartlore, setsmartlore, setnbt)

        if (args.length == 0) {
            new ItemCreatorGUI(player);
            return;
        }

        final String argument = args[0].toLowerCase();

        switch (argument) {
            case "open" -> new ItemCreatorGUI(player);
            case "new" -> new ItemCreatorGUI(player, true);
        }

        if (args.length >= 2) {
            final ItemCreator creator = Main.getItemCreator().getCreatorOrCreate(player);
            final String string = Chat.arrayToString(args, 1);

            switch (argument) {
                case "import" -> {
                    final String decode = "null";
                    player.sendMessage(decode);
                }

                case "addsmartlore" -> {
                    creator.addSmartLore(string);
                    new LoreSubGUI(player);
                }

                case "setsmartlore" -> {
                    creator.setSmartLore(string);
                    new LoreSubGUI(player);
                }

                case "setheadtexture" -> {
                    creator.setHeadTexture(string);
                    new ItemCreatorGUI(player);
                }

                case "setnbt" -> {
                    creator.setNbt(string);
                    new ItemCreatorGUI(player);
                }
            }
        }
    }

    @Override
    protected List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return completerSort(validArguments, args);
        }
        return super.tabComplete(sender, args);
    }
}
