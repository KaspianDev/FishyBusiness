package com.github.kaspiandev.fishybusiness.exception;

public class PluginLoadFailureException extends Exception {

    public PluginLoadFailureException(String message) {
        super(message);
    }

    public PluginLoadFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginLoadFailureException(Throwable cause) {
        super(cause);
    }

}
