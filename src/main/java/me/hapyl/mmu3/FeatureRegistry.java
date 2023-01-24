package me.hapyl.mmu3;

import me.hapyl.mmu3.feature.*;
import me.hapyl.mmu3.feature.brush.BrushManager;
import me.hapyl.mmu3.feature.candle.CandleController;
import me.hapyl.mmu3.feature.designer.Designer;
import me.hapyl.mmu3.feature.itemcreator.ItemCreatorFeature;
import me.hapyl.mmu3.feature.lastlocation.LastLocation;
import me.hapyl.mmu3.feature.specialblocks.SpecialBlocks;
import me.hapyl.mmu3.feature.standeditor.StandEditor;
import me.hapyl.mmu3.feature.statechanger.StateChanger;
import me.hapyl.mmu3.feature.warp.Warps;
import me.hapyl.mmu3.outcast.fishing.Fishing;
import me.hapyl.mmu3.outcast.hypixel.slayer.Slayer;

public final class FeatureRegistry {

    public final StateChanger stateChanger;
    public final StandEditor standEditor;
    public final SpecialBlocks specialBlocks;
    public final Calculate calculate;
    public final LastLocation lastLocation;
    public final ServerPro serverPro;
    public final ItemCreatorFeature itemCreator;
    public final CandleController candleController;
    public final BrushManager brushManager;
    public final Designer designer;
    public final ColoredSign coloredSign;
    public final Slayer slayer;
    public final CommandBlockPreview commandBlockPreview;
    public final EmptyCommandBlockLocator cbLocator;
    public final Fishing fishing;
    public final Warps warps;

    public FeatureRegistry(Main main) {
        stateChanger = new StateChanger(main);
        standEditor = new StandEditor(main);
        specialBlocks = new SpecialBlocks(main);
        calculate = new Calculate(main);
        lastLocation = new LastLocation(main);
        serverPro = new ServerPro(main);
        itemCreator = new ItemCreatorFeature(main);
        candleController = new CandleController(main);
        brushManager = new BrushManager(main);
        designer = new Designer(main);
        coloredSign = new ColoredSign(main);
        slayer = new Slayer(main);
        commandBlockPreview = new CommandBlockPreview(main);
        cbLocator = new EmptyCommandBlockLocator(main);
        fishing = new Fishing(main);
        warps = new Warps(main);

        // These features don't require to be accessed
    }

    public boolean isEnabled(Feature feature) {
        return feature.isEnabled();
    }

}
