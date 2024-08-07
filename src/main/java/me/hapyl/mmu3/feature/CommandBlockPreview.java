package me.hapyl.mmu3.feature;

import com.google.common.collect.Sets;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.PersistentPlayerData;
import me.hapyl.mmu3.message.Message;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.chat.LazyEvent;
import me.hapyl.eterna.module.entity.packet.NMSEntityType;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.util.Nulls;
import me.hapyl.eterna.module.util.Validate;
import net.minecraft.world.entity.monster.EntityShulker;
import net.minecraft.world.level.block.entity.TileEntityCommand;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.UUID;

public class CommandBlockPreview extends Feature implements Runnable, Listener {

    private final Set<String> commands;

    public CommandBlockPreview(Main mmu3plugin) {
        super(mmu3plugin);
        commands = Sets.newHashSet();

        registerRunnable(3L);
    }

    @EventHandler()
    public void handleVanillaCommands(PlayerCommandSendEvent ev) {
        if (!commands.isEmpty()) {
            return;
        }

        for (String command : ev.getCommands()) {
            if (!command.contains("minecraft:")) {
                continue;
            }

            commands.add(StringUtils.substringAfter(command, ":"));
        }
    }

    @Override
    public void run() {
        Bukkit
                .getOnlinePlayers()
                .stream()
                .filter(player -> player.isOp() && PersistentPlayerData.getData(player).isCommandPreview())
                .forEach(this::execute);
    }

    @EventHandler()
    public void handleCopyCommand(PlayerSwapHandItemsEvent ev) {
        final Player player = ev.getPlayer();
        if (!player.isOp() || !PersistentPlayerData.getData(player).isCommandPreview()) {
            return;
        }

        final String command = getCommand(player);
        if (command == null || command.isEmpty()) {
            if (command != null) {
                Message.error(player, "This command block doesn't have any command!");
            }
            return;
        }

        ev.setCancelled(true);

        final String commandShort = command.length() > 10 ? command.substring(0, 10) + "..." : command;
        Message.clickHover(
                player,
                LazyEvent.copyToClipboard(command),
                LazyEvent.showText("&eClick to copy!"),
                "&e&lCLICK HERE &7to copy '%s'!".formatted(commandShort)
        );
    }

    @Nullable
    private String getCommand(Player player) {
        final Block block = player.getTargetBlockExact(50);
        if (!isCommandBlock(block)) {
            return null;
        }

        final CommandBlock commandBlock = (CommandBlock) block.getState();
        final String command = commandBlock.getCommand();
        return command.startsWith("/") ? command.substring(1) : command;
    }

    private ChatColor getColor(Player player) {
        final Block block = player.getTargetBlockExact(50);

        if (!isCommandBlock(block)) {
            return ChatColor.WHITE;
        }

        final Material type = block.getType();
        final CommandBlock commandBlock = (CommandBlock) block.getState();

        try {
            final TileEntityCommand tile = (TileEntityCommand) FieldUtils.readField(commandBlock, "tileEntity", true);
            final boolean isAutomatic = tile.d();

            if (!isAutomatic) {
                return ChatColor.WHITE;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return switch (type) {
            case COMMAND_BLOCK -> ChatColor.GREEN;
            case CHAIN_COMMAND_BLOCK -> ChatColor.AQUA;
            case REPEATING_COMMAND_BLOCK -> ChatColor.LIGHT_PURPLE;
            default -> ChatColor.RED;
        };
    }

    private void execute(Player player) {
        final String command = getCommand(player);

        if (command == null) {
            return;
        }

        if (command.isEmpty() || command.isBlank()) {
            Chat.sendActionbar(player, "&f&oEmpty");
            return;
        }

        if (command.length() >= 120) {
            final String commandSignature = command.split(" ")[0];
            Chat.sendActionbar(player, "&f&oCommand is too long! &e(%s)".formatted(commandSignature));
        }
        else {
            Chat.sendActionbar(player, colorCommand(command));
        }

        Nulls.runIfNotNull(player.getTargetBlockExact(50), b -> outlineCommandBlock(player, b.getLocation(), getColor(player)));
    }

    private void outlineCommandBlock(Player player, Location location, ChatColor color) {
        final World world = location.getWorld();
        if (world == null) {
            return;
        }

        final EntityShulker entity = new EntityShulker(NMSEntityType.SHULKER, Reflect.getMinecraftWorld(world));
        Reflect.setEntityLocation(entity, location);

        final Shulker shulker = (Shulker) entity.getBukkitEntity();

        shulker.setAI(false);
        shulker.setInvisible(true);
        shulker.setInvulnerable(true);
        shulker.setGlowing(true);

        final Team team = fetchTeam(shulker.getUniqueId(), player, color);

        Reflect.createEntity(entity, player);
        Reflect.updateMetadata(entity, player);

        runTaskLater(() -> {
            Reflect.destroyEntity(entity, player);
            team.unregister();
        }, 3L);
    }

    private Team fetchTeam(UUID uuid, Player player, ChatColor color) {
        final Scoreboard scoreboard = player.getScoreboard();
        final String uuidString = uuid.toString();
        Team team = scoreboard.getTeam(uuidString);

        if (team == null) {
            team = scoreboard.registerNewTeam(uuidString);
            team.setColor(color);
            team.addEntry(uuidString);
        }

        return team;
    }

    private String colorCommand(String command) {
        final StringBuilder builder = new StringBuilder(ChatColor.WHITE.toString());

        int numbers = 0;
        for (String str : command.split(" ")) {

            // Minecraft prefix
            if (contains(str, "minecraft:", ".")) {
                builder.append(ChatColor.AQUA);
            }

            // Selectors
            if (contains(str, "@a", "@p", "@e", "@s", "@r")) {
                builder.append(ChatColor.YELLOW);
            }

            // Numbers and ~
            if (contains(str, "~", "^") || containsNumbers(str)) {
                switch (numbers) {
                    case 0 -> builder.append(ChatColor.GREEN);
                    case 1 -> builder.append(ChatColor.YELLOW);
                    case 2 -> builder.append(ChatColor.AQUA);
                }

                numbers = numbers >= 2 ? 0 : numbers + 1;
            }

            // Commands
            if (commands.contains(str)) {
                builder.append(ChatColor.LIGHT_PURPLE);
            }

            builder.append(str).append(ChatColor.WHITE).append(" ");
        }

        return builder.toString().trim();
    }

    private boolean containsNumbers(String str) {
        for (char c : str.toCharArray()) {
            if (Validate.isInt(c) || Validate.isFloat(c) || Validate.isDouble(c) || Validate.isShort(c)) {
                return true;
            }
        }

        return false;
    }

    private boolean contains(String str, String... other) {
        str = str.toLowerCase();

        if (other.length == 1) {
            return str.toLowerCase().contains(other[0].toLowerCase());
        }

        for (String s : other) {
            if (str.contains(s.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    public static boolean isCommandBlock(Block block) {
        if (block == null) {
            return false;
        }

        return switch (block.getType()) {
            case COMMAND_BLOCK, CHAIN_COMMAND_BLOCK, REPEATING_COMMAND_BLOCK -> true;
            default -> false;
        };
    }

}
