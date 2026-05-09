package me.hapyl.mmu3.command;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.standeditor.StandEditor;
import me.hapyl.mmu3.feature.standeditor.StandEditorData;
import me.hapyl.mmu3.feature.standeditor.StandEditorGUI;
import me.hapyl.mmu3.MMULogger;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class MMUOperationArmorStandEditor implements MMUOperation {

    @Override
    public @NotNull String name() {
        return "armor_stand_editor";
    }

    @Override
    public @NotNull String description() {
        return "Opens the armor stand editor for the target or the closest armor stand.";
    }

    @Override
    public void process(@NotNull Player player, @NotNull ArgumentList args) {
        // Prioritize target armor stand
        ArmorStand armorStand = null;

        Entity targetEntity = player.getTargetEntity(10, true);

        // If not targeting entity or not armor stand, get the closest stand
        if (!(targetEntity instanceof ArmorStand)) {
            final Location location = player.getLocation();

            armorStand = player.getNearbyEntities(10, 10, 10)
                    .stream()
                    .filter(ArmorStand.class::isInstance)
                    .map(ArmorStand.class::cast)
                    .min(Comparator.comparingDouble(entity -> entity.getLocation().distanceSquared(location)))
                    .orElse(null);
        }

        if (armorStand == null) {
            MMULogger.error(player, Component.text("Could not find any armor stands to edit!"));
            return;
        }

        final StandEditor standEditor = Main.featureRegistry().standEditor;

        if (standEditor.isTaken(armorStand)) {
            MMULogger.error(player, Component.text("This armors stand is already being edited!"));
            return;
        }

        final StandEditorData editorData = standEditor.getData(player);
        editorData.edit(armorStand);

        new StandEditorGUI(player, editorData);
    }

}
