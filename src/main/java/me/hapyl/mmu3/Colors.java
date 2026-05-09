package me.hapyl.mmu3;

import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public final class Colors {

    private Colors() {
    }

    public static final TextColor BRAND_COLOR = TextColor.color(0xED3E52);
    public static final TextColor TEXT_COLOR = TextColor.color(0xABCDEF);
    public static final TextColor SUCCESS = TextColor.color(0xEDC25);
    public static final TextColor ERROR = TextColor.color(0xDC1111);
    public static final TextColor INTERACTION = TextColor.color(0xDC7E2A);

}
