package com.github.kaspiandev.fishybusiness.config;

public enum Message {

    COMMAND_NO_PERMISSIONS("command.no-permissions");

    private final String path;

    Message(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
