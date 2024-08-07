package me.hapyl.mmu3.listener;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.utils.InjectListener;
import me.hapyl.eterna.module.chat.Chat;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EntityListener extends InjectListener {
    public EntityListener(Main main) {
        super(main);
    }

    // This allows entity naming with '&' as color codes.
    @EventHandler()
    public void handleEntitySpawn(EntitySpawnEvent ev) {
        final Entity entity = ev.getEntity();
        final String entityName = entity.getCustomName();

        if (entityName == null || !entityName.contains("&")) {
            return;
        }

        final String formatName = Chat.format(entityName);
        entity.setCustomName(formatName);
        Message.broadcastAdmins("Formatted %s's custom name to: '%s'.", Chat.capitalize(entity.getType()), formatName);
    }

}
