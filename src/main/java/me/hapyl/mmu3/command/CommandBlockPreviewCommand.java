package me.hapyl.mmu3.command;

import me.hapyl.mmu3.PersistentPlayerData;
import me.hapyl.mmu3.message.Message;
import me.hapyl.eterna.module.command.SimplePlayerAdminCommand;
import org.bukkit.entity.Player;

public class CommandBlockPreviewCommand extends SimplePlayerAdminCommand {
    public CommandBlockPreviewCommand(String name) {
        super(name);
        setDescription("Allows to switch command block preview feature.");
        setAliases("preview", "cbp");
    }

    @Override
    protected void execute(Player player, String[] args) {
        final PersistentPlayerData data = PersistentPlayerData.getData(player);
        data.setCommandPreview(!data.isCommandPreview());
        Message.success(player, "Command block preview is now %s.", data.isCommandPreview() ? "enabled" : "disabled");

        if (data.isCommandPreview()) {
            Message.info(player, "&oYour target command block will be highlighted and it's command will show in actionbar.");
            Message.info(player, "&oHighlight color is based on a command block type if it's active, &fwhite &7&ootherwise.");
        }
    }
}
