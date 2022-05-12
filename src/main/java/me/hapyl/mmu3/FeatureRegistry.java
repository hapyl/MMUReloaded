package me.hapyl.mmu3;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.feature.Calculate;
import me.hapyl.mmu3.feature.Feature;
import me.hapyl.mmu3.feature.ServerPro;
import me.hapyl.mmu3.feature.itemcreator.ItemCreatorFeature;
import me.hapyl.mmu3.feature.lastlocation.LastLocation;
import me.hapyl.mmu3.feature.specialblocks.SpecialBlocks;
import me.hapyl.mmu3.feature.standeditor.StandEditor;
import me.hapyl.mmu3.feature.statechanger.StateChanger;

import java.util.Map;

public final class FeatureRegistry {

    private final Map<Feature, Boolean> featureStatus;

    public StateChanger stateChanger;
    public StandEditor standEditor;
    public SpecialBlocks specialBlocks;
    public Calculate calculate;
    public LastLocation lastLocation;
    public ServerPro serverPro;
    public ItemCreatorFeature itemCreator;

    public FeatureRegistry(Main main) {
        featureStatus = Maps.newHashMap();

        stateChanger = new StateChanger(main);
        standEditor = new StandEditor(main);
        specialBlocks = new SpecialBlocks(main);
        calculate = new Calculate(main);
        lastLocation = new LastLocation(main);
        itemCreator = new ItemCreatorFeature(main);
    }

    public void setFeatureStatus(Feature feature, boolean flag) {
        featureStatus.put(feature, flag);
    }

    public boolean getStatus(Feature feature) {
        return featureStatus.getOrDefault(feature, false);
    }

}
