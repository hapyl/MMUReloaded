package me.hapyl.mmu3;

import me.hapyl.mmu3.feature.*;
import me.hapyl.mmu3.feature.action.PlayerActionFeature;
import me.hapyl.mmu3.feature.activity.WorldActivity;
import me.hapyl.mmu3.feature.banner.BannerEditor;
import me.hapyl.mmu3.feature.bb.BoundingBoxManager;
import me.hapyl.mmu3.feature.block.BlockReplacer;
import me.hapyl.mmu3.feature.brush.BrushManager;
import me.hapyl.mmu3.feature.candle.CandleController;
import me.hapyl.mmu3.feature.dotcommand.DotCommand;
import me.hapyl.mmu3.feature.itemcreator.ItemCreatorFeature;
import me.hapyl.mmu3.feature.lastlocation.LastLocation;
import me.hapyl.mmu3.feature.search.Search;
import me.hapyl.mmu3.feature.specialblocks.SpecialBlocks;
import me.hapyl.mmu3.feature.standeditor.StandEditor;
import me.hapyl.mmu3.feature.statechanger.StateChanger;
import me.hapyl.mmu3.feature.trim.TrimManager;
import me.hapyl.mmu3.feature.warp.Warps;
import me.hapyl.mmu3.util.input.InputListenerHandler;

public final class FeatureRegistry {

    public final StateChanger stateChanger;
    public final StandEditor standEditor;
    public final SpecialBlocks specialBlocks;
    public final Calculate calculate;
    public final LastLocation lastLocation;
    public final ItemCreatorFeature itemCreator;
    public final CandleController candleController;
    public final BrushManager brushManager;
    public final ColoredSign coloredSign;
    public final CommandBlockPreview commandBlockPreview;
    public final EmptyCommandBlockLocator cbLocator;
    public final Warps warps;
    public final WorldActivity worldActivity;
    public final BoundingBoxManager boundingBoxManager;
    public final BlockReplacer replacer;
    public final Search search;
    public final TrimManager trimManager;
    public final DecorativePotFeature decorativePot;
    public final PlayerActionFeature playerActionFeature;
    public final UndoManager undoManager;
    public final BannerEditor bannerEditor;
    public final InputListenerHandler inputHandler;

    public FeatureRegistry(Main main) {
        stateChanger = new StateChanger(main);
        standEditor = new StandEditor(main);
        specialBlocks = new SpecialBlocks(main);
        calculate = new Calculate(main);
        lastLocation = new LastLocation(main);
        itemCreator = new ItemCreatorFeature(main);
        candleController = new CandleController(main);
        brushManager = new BrushManager(main);
        coloredSign = new ColoredSign(main);
        commandBlockPreview = new CommandBlockPreview(main);
        cbLocator = new EmptyCommandBlockLocator(main);
        warps = new Warps(main);
        worldActivity = new WorldActivity(main);
        boundingBoxManager = new BoundingBoxManager(main);
        replacer = new BlockReplacer(main);
        search = new Search(main);
        trimManager = new TrimManager(main);
        decorativePot = new DecorativePotFeature(main);
        playerActionFeature = new PlayerActionFeature(main);
        undoManager = new UndoManager(main);
        bannerEditor = new BannerEditor(main);
        inputHandler = new InputListenerHandler(main);

        // These features don't require to be accessed
        new DotCommand(main);
    }

    public boolean isEnabled(Feature feature) {
        return feature.isEnabled();
    }

}
