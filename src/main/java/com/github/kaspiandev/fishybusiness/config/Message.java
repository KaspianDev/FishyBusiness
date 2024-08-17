package com.github.kaspiandev.fishybusiness.config;

public enum Message {

    COMMAND_NO_PERMISSIONS("command.no-permissions"),
    COMMAND_NO_ARGUMENTS("command.no-arguments"),
    COMMAND_INVALID_SUBCOMMAND("command.invalid-subcommand"),

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
