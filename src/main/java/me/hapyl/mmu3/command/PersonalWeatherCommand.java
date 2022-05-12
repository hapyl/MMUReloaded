package me.hapyl.mmu3.command;

import com.google.common.collect.Lists;
import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import kz.hapyl.spigotutils.module.util.Validate;
import me.hapyl.mmu3.Message;
import org.bukkit.WeatherType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PersonalWeatherCommand extends SimplePlayerAdminCommand {

    private final List<String> validArguments = Lists.newArrayList();

    public PersonalWeatherCommand(String name) {
        super(name);
        setDescription("Allows to change player's weather.");
        setAliases("pweather");

        for (WeatherType value : WeatherType.values()) {
            validArguments.add(value.name().toLowerCase());
        }

        validArguments.add("reset");
    }

    @Override
    protected void execute(Player player, String[] args) {
        // pweather (Weather)
        if (args.length != 1) {
            Message.INVALID_ARGUMENT_SIZE.send(player, 1);
            return;
        }

        if (args[0].equalsIgnoreCase("reset")) {
            Message.success(player, "Reset your weather.");
            return;
        }

        final WeatherType type = Validate.getEnumValue(WeatherType.class, args[0]);
        if (type == null) {
            Message.error(player, "%s is invalid weather type!", args[0]);
            return;
        }

        player.setPlayerWeather(type);
        Message.success(player, "Changed your weather to %s.", Chat.capitalize(type));
    }

    @Override
    protected List<String> tabComplete(CommandSender sender, String[] args) {
        return completerSort(validArguments, args);
    }

}
