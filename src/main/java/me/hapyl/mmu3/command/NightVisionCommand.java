package me.hapyl.mmu3.command;

import me.hapyl.mmu3.message.Message;
import me.hapyl.eterna.module.command.SimplePlayerAdminCommand;
import me.hapyl.eterna.module.player.PlayerLib;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class NightVisionCommand extends SimplePlayerAdminCommand {
    public NightVisionCommand(String name) {
        super(name);
        setDescription("Allows to switch between night vision.");
        setAliases("nv");
    }

    @Override
    protected void execute(Player player, String[] args) {
        if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            Message.success(player, "Night Vision is now disabled.");
            return;
        }

        PlayerLib.addEffect(player, PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1);
        Message.success(player, "Night Vision is now enabled.");
    }
}
