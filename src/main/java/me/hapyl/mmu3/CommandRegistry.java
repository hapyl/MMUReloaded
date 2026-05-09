package me.hapyl.mmu3;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.command.*;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class CommandRegistry {
    
    public final Map<String, MMUOperation> operations;
    
    public CommandRegistry() {
        // Since the plugin going public, we now use /mmu (operation)
        Bukkit.getCommandMap().register("mmu", new MMUCommand("mmu", this));
        
        // Register operations
        operations = Maps.newHashMap();
        
        register(
                new MMUOperationArmorStandEditor(),
                new MMUOperationBack(),
                new MMUOperationBannerEditor(),
                new MMUOperationCalculate(),
                new MMUOperationCandle(),
                new MMUOperationCenter(),
                new MMUOperationColor(),
                new MMUOperationCommandBlockPreview(),
                new MMUOperationEntityRemover(),
                new MMUOperationHat(),
                new MMUOperationLineOfSight(),
                new MMUOperationLockContainer(),
                new MMUOperationNightVision(),
                new MMUOperationOutline(),
                new MMUOperationParticle(),
                new MMUOperationPersonalTime(),
                new MMUOperationPersonalWeather(),
                new MMUOperationPing(),
                new MMUOperationRelative(),
                new MMUOperationReload(),
                new MMUOperationRemoveEntity(),
                new MMUOperationRoll(),
                new MMUOperationSightBlock(),
                new MMUOperationSkullDisplay(),
                new MMUOperationStateChanger(),
                new MMUOperationTrim(),
                new MMUOperationWiki()
        );
    }
    
    private void register(@NotNull MMUOperation... operations) {
        for (MMUOperation operation : operations) {
            final String operationName = operation.name();
            
            if (this.operations.containsKey(operationName)) {
                throw new IllegalArgumentException("Duplicate operation registration: %s!".formatted(operationName));
            }
            
            this.operations.put(operationName.toLowerCase(), operation);
        }
    }
    
}
