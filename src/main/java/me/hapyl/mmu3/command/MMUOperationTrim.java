package me.hapyl.mmu3.command;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.trim.CachedTrimData;
import me.hapyl.mmu3.feature.trim.EnumTrimMaterial;
import me.hapyl.mmu3.feature.trim.EnumTrimPattern;
import me.hapyl.mmu3.feature.trim.TrimManager;
import me.hapyl.mmu3.MMULogger;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class MMUOperationTrim implements MMUOperation {

    @Override
    public @NotNull String name() {
        return "trim";
    }

    @Override
    public @NotNull String description() {
        return "Allows armor trim management.";
    }

    @Override
    public @NotNull MMUCompletion completions() {
        return MMUCompletion.builder()
                .where(1, Arrays.stream(EnumTrimPattern.values()).map(Enum::name).map(String::toLowerCase).toList())
                .where(2, Arrays.stream(EnumTrimMaterial.values()).map(Enum::name).map(String::toLowerCase).toList());
    }

    @Override
    public void process(@NotNull Player player, @NotNull ArgumentList args) {
        final TrimManager trimManager = Main.featureRegistry().trimManager;

        switch (args.length) {
            // mmu trim
            case 0 -> trimManager.enterEditor(player);

            // mmu trim (id)
            case 1 -> {
                final int id = args.get(0).toInt();
                final CachedTrimData trimData = trimManager.getData(id);

                if (trimData == null) {
                    MMULogger.error(player, Component.text("Could not find trim with id %s!".formatted(id)));
                    return;
                }

                trimData.give(player);
                MMULogger.success(player, Component.text("Gave you trim with id %s!".formatted(id)));
            }

            // mmu trim (pattern) (material)
            case 2 -> {
                final PlayerInventory inventory = player.getInventory();
                final ItemStack itemStack = inventory.getItemInMainHand();

                if (itemStack.isEmpty()) {
                    MMULogger.error(player, Component.text("You must be holding an item!"));
                    return;
                }

                if (!(itemStack.getItemMeta() instanceof ArmorMeta armorMeta)) {
                    MMULogger.error(player, Component.text("This item cannot be trimmed!"));
                    return;
                }

                final EnumTrimPattern trimPattern = args.get(1).toEnum(EnumTrimPattern.class).orElse(null);
                final EnumTrimMaterial trimMaterial = args.get(2).toEnum(EnumTrimMaterial.class).orElse(null);

                if (trimPattern == null) {
                    MMULogger.error(player, Component.text("Invalid trim pattern %s!".formatted(args.get(1))));
                    return;
                }

                if (trimMaterial == null) {
                    MMULogger.error(player, Component.text("Invalid trim material %s!".formatted(args.get(2))));
                    return;
                }

                armorMeta.setTrim(new ArmorTrim(trimMaterial.bukkit, trimPattern.bukkit));
            }
        }
    }

}