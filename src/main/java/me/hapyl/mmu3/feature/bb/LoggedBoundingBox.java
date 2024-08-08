package me.hapyl.mmu3.feature.bb;

import me.hapyl.mmu3.message.Message;
import me.hapyl.eterna.module.player.PlayerLib;
import me.hapyl.eterna.module.reflect.BoundingBox;
import me.hapyl.eterna.module.util.BukkitUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class LoggedBoundingBox extends BoundingBox {
    public LoggedBoundingBox(Player player) {
        super(player);
    }

    @Override
    public boolean setStart(Player player) {
        final boolean bool = super.setStart(player);

        if (bool) {
            Message.success(player, "Set start position to %s.", getStartString());
            PlayerLib.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 2);

            super.show();
        }
        else {
            Message.error(player, "Could not set position. Not valid block in sight.");
        }

        return bool;
    }

    @Override
    public boolean setEnd(Player player) {
        final boolean bool = super.setEnd(player);

        if (bool) {
            Message.success(player, "Set end position to %s.", getEndString());
            PlayerLib.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 2);
        }
        else {
            Message.error(player, "Could not set position. Not valid block in sight.");
        }

        return bool;
    }

    @Override
    public boolean show() {
        final boolean show = super.show();

        if (show) {
            Message.success(getPlayer(), "Drew the bounding box.");
        }
        else {
            Message.error(getPlayer(), "Could not draw bounding box, either not defined or too big. (>48)");
        }

        return show;
    }

    @Override
    public void hide() {
        super.hide();
    }

    public String getStartString() {
        final Location location = getStart();
        return location == null ? "INVALID_LOCATION" : BukkitUtils.locationToString(location);
    }

    public String getEndString() {
        final Location location = getEnd();
        return location == null ? "INVALID_LOCATION" : BukkitUtils.locationToString(location);
    }
}
