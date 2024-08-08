package me.hapyl.mmu3.feature.trim;

import me.hapyl.eterna.module.util.Enums;

public enum TrimMode {

    PATTERN {
        @Override
        public void next(TrimData data) {
            data.setPattern(Enums.getNextValue(EnumTrimPattern.class, data.getPattern()));
        }

        @Override
        public void previous(TrimData data) {
            data.setPattern(Enums.getPreviousValue(EnumTrimPattern.class, data.getPattern()));
        }

        @Override
        public String current(TrimData data) {
            return data.getPattern().name();
        }
    },
    MATERIAL {
        @Override
        public void next(TrimData data) {
            data.setMaterial(Enums.getNextValue(EnumTrimMaterial.class, data.getMaterial()));
        }

        @Override
        public void previous(TrimData data) {
            data.setMaterial(Enums.getPreviousValue(EnumTrimMaterial.class, data.getMaterial()));
        }

        @Override
        public String current(TrimData data) {
            return data.getMaterial().name();
        }
    };

    public String current(TrimData data) {
        return null;
    }

    public void next(TrimData data) {
        throw new IllegalStateException();
    }

    public void previous(TrimData data) {
        throw new IllegalStateException();
    }
}
