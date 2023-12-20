package me.hapyl.mmu3.feature.brush;

import me.hapyl.mmu3.feature.brush.archive.BrushCircle;
import me.hapyl.mmu3.feature.brush.archive.BrushGradient;

import javax.annotation.Nullable;

public enum Brushes {

    NONE(null),
    CIRCLE(new BrushCircle()),
    GRADIENT(new BrushGradient()),
    ;

    private final Brush brush;

    Brushes(Brush brush) {
        this.brush = brush;
    }

    @Nullable
    public Brush getBrush() {
        return brush;
    }
}
