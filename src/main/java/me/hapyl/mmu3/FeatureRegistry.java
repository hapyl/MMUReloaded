package me.hapyl.mmu3;

import me.hapyl.mmu3.feature.*;
import me.hapyl.mmu3.feature.banner.BannerEditor;
import me.hapyl.mmu3.feature.brush.BrushManager;
import me.hapyl.mmu3.feature.candle.CandleFeature;
import me.hapyl.mmu3.feature.LastLocationFeature;
import me.hapyl.mmu3.feature.outline.PlayerOutlineFeature;
import me.hapyl.mmu3.feature.search.Search;
import me.hapyl.mmu3.feature.standeditor.StandEditor;
import me.hapyl.mmu3.feature.statechanger.StateChanger;
import me.hapyl.mmu3.feature.trim.TrimManager;
import me.hapyl.mmu3.util.input.InputListenerHandler;
import org.jetbrains.annotations.NotNull;

public final class FeatureRegistry {
    
    public final StateChanger stateChanger;
    public final StandEditor standEditor;
    public final Calculate calculate;
    public final LastLocationFeature lastLocationFeature;
    public final CandleFeature candleFeature;
    public final BrushManager brushManager;
    public final ColoredSign coloredSign;
    public final CommandBlockPreview commandBlockPreview;
    public final EmptyCommandBlockLocator cbLocator;
    public final PlayerOutlineFeature playerOutlineManager;
    public final Search search;
    public final TrimManager trimManager;
    public final DecorativePotFeature decorativePot;
    public final UndoManager undoManager;
    public final BannerEditor bannerEditor;
    public final InputListenerHandler inputHandler;
    
    public FeatureRegistry(@NotNull Main main) {
        stateChanger = new StateChanger(main);
        standEditor = new StandEditor(main);
        calculate = new Calculate(main);
        lastLocationFeature = new LastLocationFeature(main);
        candleFeature = new CandleFeature(main);
        brushManager = new BrushManager(main);
        coloredSign = new ColoredSign(main);
        commandBlockPreview = new CommandBlockPreview(main);
        cbLocator = new EmptyCommandBlockLocator(main);
        playerOutlineManager = new PlayerOutlineFeature(main);
        search = new Search(main);
        trimManager = new TrimManager(main);
        decorativePot = new DecorativePotFeature(main);
        undoManager = new UndoManager(main);
        bannerEditor = new BannerEditor(main);
        inputHandler = new InputListenerHandler(main);
    }
    
}
