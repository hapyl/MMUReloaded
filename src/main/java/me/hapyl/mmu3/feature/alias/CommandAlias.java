package me.hapyl.mmu3.feature.alias;

public enum CommandAlias {

    WE_UNDO("/undo", "u"),
    ;


    private final String command;
    private final String[] aliases;
    private boolean register;

    CommandAlias(String command, String... aliases) {
        this.command = command;
        this.aliases = aliases;
        this.register = false;
    }

    public String getCommand() {
        return command;
    }

    public void register() {

    }
}
