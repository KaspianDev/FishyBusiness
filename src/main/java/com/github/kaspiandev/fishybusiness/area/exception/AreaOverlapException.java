package com.github.kaspiandev.fishybusiness.area.exception;

public class AreaOverlapException extends RuntimeException {

    public AreaOverlapException() {
        super("Cannot register Area because it would overlap another area.");
    }

}
