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
    AREA_NO_WORLD("area.no-world"),
    AREA_INVALID_ID("area.invalid-id"),
    AREA_NO_ID("area.no-id"),
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
    REWARD_OVERLAP("reward.overlap"),
    REWARD_EMPTY_CONTAINER("reward.empty-container"),
    REWARD_INVALID_REWARD("reward.invalid-reward"),
    REWARD_NO_TITLE_PROPERTIES("reward.no-title-properties"),
    REWARD_NO_MESSAGE_TYPE("reward.no-message-type"),
    REWARD_NO_SOUND("reward.no-sound"),
    REWARD_NO_PITCH_OR_VOLUME("reward.no-pitch-or-volume"),
    REWARD_NO_MESSAGE("reward.no-message"),
    REWARD_NO_XP("reward.no-xp"),
    REWARD_NO_MONEY("reward.no-money"),
    REWARD_NO_POINTS("reward.no-points"),
    REWARD_NO_ITEM("reward.no-item"),
    REWARD_UNKNOWN_ADAPTER("reward.unknown-adapter"),

    POINTS_NO_PLAYER("points.no-player"),
    POINTS_GIVE_NO_AMOUNT("points.give-no-amount"),
    POINTS_GIVEN("points.given"),
    POINTS_REMOVE_NO_AMOUNT("points.remove-no-amount"),
    POINTS_REMOVED("points.removed"),
    POINTS_INVALID_POINTS("points.invalid-points"),
    POINTS_PLAYER_POINTS("points.player-points"),
    POINTS_TOP_ENTRY_SINGLE("points.top-entry-single"),
    POINTS_TOP_HEADER("points.top-header"),
    POINTS_TOP_LIST_ENTRY("points.top-list-entry"),
    POINTS_TOP_ENTRY("points.top-entry"),

    RELOAD_RELOADED_ALL("reload.reloaded-all"),
    RELOAD_RELOADED("reload.reloaded"),
    RELOAD_ERROR("reload.error"),
    RELOAD_ERROR_ALL("reload.error-all"),
    RELOAD_UNKNOWN_TYPE("reload.unknown-type"),

    MAIL_CANNOT_CLAIM("mail.cannot-claim");

    private final String path;

    Message(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
