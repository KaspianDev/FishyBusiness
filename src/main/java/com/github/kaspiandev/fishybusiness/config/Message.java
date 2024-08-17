package com.github.kaspiandev.fishybusiness.config;

public enum Message {

    ;

    private final String path;

    Message(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
