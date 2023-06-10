package me.hapyl.mmu3.command;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.bb.BoundingBoxManager;
import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.chat.LazyEvent;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.reflect.BoundingBox;
import org.bukkit.entity.Player;

public class BoundingBoxCommand extends SimplePlayerAdminCommand {

    public BoundingBoxCommand(String name) {
        super(name);

        setDescription("Draws a bounding box outline between two points.");
        setAliases("bb");

        addCompleterValues(1, "clear", "pos1", "pos2", "1", "2", "execute", "spigot");
    }

    @Override
    protected void execute(Player player, String[] args) {
        final BoundingBoxManager boundingBoxManager = Main.getRegistry().boundingBoxManager;

        if (args.length == 0) {
            final BoundingBox outline = boundingBoxManager.getOrCreateOutline(player);

            if (outline.getStart() == null) {
                outline.setStart(player);
            }
            else {
                outline.setEnd(player);
            }

            if (outline.isDefined()) {
                outline.show();
            }

            return;
        }

        final BoundingBox outline = boundingBoxManager.getOrCreateOutline(player);

        switch (args[0].toLowerCase()) {
            case "clear" -> {
                boundingBoxManager.remove(player);
                Message.success(player, "Cleared the bounding box!");
            }
            case "pos1", "1" -> {
                outline.setStart(player);
            }
            case "pos2", "2" -> {
                outline.setEnd(player);
                outline.show();
            }
            case "execute" -> {
                final String execute = "execute as @a[x=%s, y=%s, z=%s, dx=%s, dy=%s, dz=%s] at @s".formatted(
                        outline.getMinX(),
                        outline.getMinY(),
                        outline.getMinZ(),
                        outline.getSizeX() - 1,
                        outline.getSizeY() - 1,
                        outline.getSizeZ() - 1
                );

                Message.clickHover(
                        player,
                        LazyEvent.copyToClipboard(execute),
                        LazyEvent.showText("&eClick to copy!"),
                        "Execute command: " + execute + " &6&lCLICK"
                );
            }
            case "spigot" -> {
                final String spigot = "%s, %s, %s, %s, %s, %s".formatted(
                        outline.getMinX(),
                        outline.getMinY(),
                        outline.getMinZ(),
                        outline.getMaxX(),
                        outline.getMaxY(),
                        outline.getMaxZ()
                );

                Message.clickHover(
                        player,
                        LazyEvent.copyToClipboard(spigot),
                        LazyEvent.showText("&eClick to copy!"),
                        "Spigot coordinates: " + spigot + " &6&lCLICK"
                );
            }
        }

    }
}
