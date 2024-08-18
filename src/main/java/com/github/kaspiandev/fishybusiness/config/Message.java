package com.github.kaspiandev.fishybusiness.config;

public enum Message {

    COMMAND_NO_PERMISSIONS("command.no-permissions"),
    COMMAND_NO_ARGUMENTS("command.no-arguments"),
    COMMAND_INVALID_SUBCOMMAND("command.invalid-subcommand"),
    COMMAND_ONLY_PLAYERS("command.only-players"),

    AREA_SAVED("area.saved"),
    AREA_OVERLAP("area.overlap"),
    AREA_NO_NAME("area.no-name"),
    AREA_CANNOT_SET("area.cannot-set"),
    AREA_UNKNOWN_ADAPTER("area.unknown-adapter"),
    AREA_TOOL_GIVEN("area.tool-given"),

    RELOAD_RELOADED_ALL("reload.reloaded-all"),
    RELOAD_RELOADED("reload.reloaded"),
    RELOAD_ERROR("reload.error"),
    RELOAD_ERROR_ALL("reload.error-all"),
    RELOAD_UNKNOWN_TYPE("reload.unknown-type");

    private final String path;

    Message(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
