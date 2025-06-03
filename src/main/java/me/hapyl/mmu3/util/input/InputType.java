package me.hapyl.mmu3.util.input;

import me.hapyl.eterna.module.chat.Chat;

import javax.annotation.Nonnull;

public enum InputType {

    W {
        @Nonnull
        @Override
        public String delimiter() {
            return "";
        }
    },
    S {
        @Nonnull
        @Override
        public String delimiter() {
            return "";
        }
    },
    A {
        @Nonnull
        @Override
        public String delimiter() {
            return "";
        }
    },
    D {
        @Nonnull
        @Override
        public String delimiter() {
            return "";
        }
    },
    F {
        @Override
        public String toString() {
            return "Swap Hands";
        }
    },
    SPACE,
    SNEAK,
    SPRINT,
    LEFT {
        @Override
        public String toString() {
            return "Left Click";
        }
    },
    RIGHT {
        @Override
        public String toString() {
            return "Right Click";
        }
    },
    START_LISTENING,
    STOP_LISTENING;

    @Nonnull
    public String delimiter() {
        return " &8&&f&l ";
    }

    @Override
    public String toString() {
        return Chat.capitalize(name());
    }
}
