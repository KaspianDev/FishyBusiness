package com.github.kaspiandev.fishybusiness.config;

public enum Message {

    COMMAND_NO_PERMISSIONS("command.no-permissions"),
    COMMAND_NO_ARGUMENTS("command.no-arguments"),
    COMMAND_INVALID_SUBCOMMAND("command.invalid-subcommand"),
    COMMAND_ONLY_PLAYERS("command.only-players"),

    AREA_SAVED("area.saved"),
    AREA_REMOVED("area.removed"),
    AREA_NO_AREA("area.no-area"),
    AREA_OVERLAP("area.overlap"),
    AREA_WORLD_MISMATCH("area.world-mismatch"),
    AREA_CANNOT_SET("area.cannot-set"),
    AREA_UNKNOWN_ADAPTER("area.unknown-adapter"),
    AREA_TOOL_GIVEN("area.tool-given"),

    REWARD_ADDED("reward.added"),
    REWARD_REMOVED("reward.removed"),
    REWARD_NO_NAME("reward.no-name"),
    REWARD_NOT_FOUND("reward.not-found"),
    REWARD_NO_COMMAND("reward.no-command"),
    REWARD_NO_WEIGHT("reward.no-weight"),
    REWARD_UNKNOWN_ADAPTER("reward.unknown-adapter"),

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
