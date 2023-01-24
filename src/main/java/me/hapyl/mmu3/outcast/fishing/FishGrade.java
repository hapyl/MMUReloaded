package me.hapyl.mmu3.outcast.fishing;

public enum FishGrade {

    /**
     * Invalid size.
     */
    INVALID(" ☠"),

    /**
     * Any fish <= 1/5 of a size median.
     */
    NORMAL(""),

    /**
     * Any fish <= 1/4 of a size median.
     */
    SMALL(" &a⭐"),

    /**
     * Any fish <= 1/3 of a size median.
     */
    BIG(" &e⭐⭐"),

    /**
     * Any fish <= 1/2 of a size median.
     */
    GIANT(" &6⭐⭐⭐"),

    /**
     * Maximum fish size.
     */
    MASSIVE(" &c&l⭐⭐⭐⭐");

    private final String suffix;

    FishGrade(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }
}
