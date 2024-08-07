package me.hapyl.mmu3.command;

import me.hapyl.mmu3.PersistentPlayerData;
import me.hapyl.mmu3.message.Message;
import me.hapyl.eterna.module.command.SimplePlayerAdminCommand;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class EntityRemovalCommand extends SimplePlayerAdminCommand {
    public EntityRemovalCommand(String name) {
        super(name);
        setDescription("Toggles entity removal mode.");
        setAliases("er", "removal");
    }

    @Override
    protected void execute(Player player, String[] args) {
        final PersistentPlayerData data = PersistentPlayerData.getData(player);
        data.setEntityRemoval(!data.isEntityRemoval());

        Message.info(
                player,
                "Entity Removal is now %s",
                data.isEntityRemoval() ? "enabled. &e&lPUNCH &7entities to remove them." : "disabled."
        );
        Message.sound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 2.0f);
    }
}
