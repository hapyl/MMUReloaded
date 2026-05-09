package me.hapyl.mmu3.command;

import me.hapyl.mmu3.CommandRegistry;
import me.hapyl.mmu3.MMULogger;
import me.hapyl.mmu3.feature.FeatureBase;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public final class MMUCommand extends Command {
    
    private static final String CHAR_ABOUT = "?";
    
    private final CommandRegistry registry;
    
    public MMUCommand(@Nonnull String name, @NotNull CommandRegistry registry) {
        super(name);
        
        this.registry = registry;
    }
    
    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            MMULogger.error(sender, Component.text("You must be a player to execute this command!"));
            return true;
        }
        
        if (!player.isOp()) {
            MMULogger.error(sender, Component.text("You must be an operator to execute this command!"));
            return true;
        }
        
        // mmu (operation) [args]
        if (args.length == 0) {
            MMULogger.error(sender, Component.text("/mmu (operation)"));
            return true;
        }
        
        final String operationName = args[0].toLowerCase();
        final MMUOperation operation = registry.operations.get(operationName);
        
        if (operation == null) {
            MMULogger.error(player, Component.text("Invalid operation: %s".formatted(operationName)));
            return true;
        }
        
        // Handle description before execution
        if (args.length == 2 && args[1].equalsIgnoreCase(CHAR_ABOUT)) {
            final String name = operation.name();
            final String description = operation.description();
            
            MMULogger.info(player, Component.text("Information about `%s`:".formatted(name), NamedTextColor.AQUA));
            MMULogger.info(player, Component.text(description, NamedTextColor.GRAY));
            return true;
        }
        
        // If operation implements FeatureBase, check whether it's enabled or not
        if (operation instanceof FeatureBase operationAsFeature && !operationAsFeature.isEnabled()) {
            MMULogger.error(
                    player,
                    Component.empty()
                             .append(Component.text("This command is currently disabled!"))
                             .append(Component.text(" (%s)".formatted(operationAsFeature.getKey()), NamedTextColor.DARK_GRAY))
            );
            return true;
        }
        
        final ArgumentList argumentList = new ArgumentList(Arrays.stream(args).skip(1).toArray(String[]::new));
        
        try {
            operation.process(player, argumentList);
        }
        catch (Exception ex) {
            MMULogger.error(player, Component.text("Error executing `%s`!".formatted(operationName)));
            MMULogger.error(player, Component.text(ex.getMessage(), NamedTextColor.YELLOW));
        }
        
        return true;
    }
    
    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        final MMUOperation operation = registry.operations.get(args[0].toLowerCase());
        
        return operation == null ? List.of() : operation.completions().complete(args.length - 1);
    }
    
}