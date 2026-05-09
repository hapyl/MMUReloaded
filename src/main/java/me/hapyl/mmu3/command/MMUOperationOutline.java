package me.hapyl.mmu3.command;

import me.hapyl.mmu3.MMULogger;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.FeatureBase;
import me.hapyl.mmu3.feature.FeatureKey;
import me.hapyl.mmu3.feature.outline.PlayerOutline;
import me.hapyl.mmu3.feature.outline.PlayerOutlineFeature;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MMUOperationOutline implements MMUOperation, FeatureBase {
    
    @Override
    public @NotNull String name() {
        return "outline";
    }
    
    @Override
    public @NotNull String description() {
        return "Draws a bounding box outline between two points and allows generating execute commands.";
    }
    
    @NotNull
    @Override
    public MMUCompletion completions() {
        return MMUCompletion.builder()
                            .where(0, List.of("pos1", "pos2", "1", "2", "clear", "execute", "bukkit"));
    }
    
    @Override
    public void process(@NotNull Player player, @NotNull ArgumentList args) {
        // mmu outline [pos1, pos2, clear, execute, bukkit]
        final PlayerOutlineFeature playerOutlineManager = Main.featureRegistry().playerOutlineManager;
        
        // If no arguments passed, we set either the start or the end automatically
        if (args.length == 0) {
            final PlayerOutline outline = playerOutlineManager.getOrCreateOutline(player);
            
            if (outline.getStart() == null) {
                outline.setStartFromTargetBlock();
            }
            else if (outline.getEnd() == null) {
                outline.setEndFromTargetBlock();
            }
            else {
                MMULogger.error(player, Component.text("Both the start and the end positions are already defined!"));
                return;
            }
            
            // If defined after setting the location, show the outline
            if (outline.isDefined()) {
                outline.show();
            }
            
            return;
        }
        
        final PlayerOutline outline = playerOutlineManager.getOrCreateOutline(player);
        
        switch (args.get(0).toString().toLowerCase()) {
            case "pos1", "1" -> {
                outline.setStartFromTargetBlock();
            }
            
            case "pos2", "2" -> {
                outline.setEndFromTargetBlock();
                
                if (outline.isDefined()) {
                    outline.show();
                }
            }
            
            case "clear" -> {
                playerOutlineManager.reset(player);
                MMULogger.success(player, Component.text("Cleared existing outline."));
            }
            
            case "execute" -> {
                final String execute = "execute as @a[x=%s, y=%s, z=%s, dx=%s, dy=%s, dz=%s] at @s".formatted(
                        outline.minX(),
                        outline.minY(),
                        outline.minZ(),
                        outline.sizeX() - 1,
                        outline.sizeY() - 1,
                        outline.sizeZ() - 1
                );
                
                MMULogger.copy(
                        player,
                        Component.text("Execute Command"),
                        ClickEvent.copyToClipboard(execute)
                );
            }
            
            case "bukkit" -> {
                final String bukkit = "%s, %s, %s, %s, %s, %s".formatted(
                        outline.minX(),
                        outline.minY(),
                        outline.minZ(),
                        outline.maxX(),
                        outline.maxY(),
                        outline.maxZ()
                );
                
                MMULogger.copy(
                        player,
                        Component.text("Bukkit Coordinates"),
                        ClickEvent.copyToClipboard(bukkit)
                );
            }
        }
    }
    
    @Override
    public @NotNull FeatureKey getKey() {
        return Main.featureRegistry().playerOutlineManager.getKey();
    }
}
