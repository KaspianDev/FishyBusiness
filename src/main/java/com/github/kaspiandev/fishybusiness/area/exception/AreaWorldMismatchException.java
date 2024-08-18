package com.github.kaspiandev.fishybusiness.area.exception;

public class AreaWorldMismatchException extends RuntimeException {

    public AreaWorldMismatchException() {
        super("Cannot create Area because worlds don't match.");
    }

}
