package me.hapyl.mmu3.feature.itemcreator;

import me.hapyl.spigotutils.module.inventory.Response;
import me.hapyl.spigotutils.module.inventory.SignGUI;
import org.bukkit.Color;
import org.bukkit.entity.Player;

public abstract class ColorSignGUI extends SignGUI {
    public ColorSignGUI(Player player) {
        super(player, "#", SignGUI.DASHED_LINE, "Enter hexadecimal", "value");
    }

    public abstract void onResponse(Color color);

    @Override
    public void onResponse(Response response) {
        String string = response.getAsString();

        // Add # if not present
        if (!string.startsWith("#")) {
            string = "#" + string;
        }
        // Trim to only have one # (In case if pasted with #)
        else {
            string = string.substring(string.lastIndexOf("#"));
        }

        // Trim to min of 7
        string = string.substring(0, Math.min(string.length(), 7));
        final java.awt.Color decode = java.awt.Color.decode(string);

        onResponse(Color.fromRGB(decode.getRed(), decode.getGreen(), decode.getBlue()));
    }
}
