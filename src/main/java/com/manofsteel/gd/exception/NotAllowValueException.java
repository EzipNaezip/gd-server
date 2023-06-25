package com.manofsteel.gd.exception;

public class NotAllowValueException extends Exception {
    private static final String DEFAULT_MESSAGE = "Not Allow Value!";

    public NotAllowValueException() {
        super(DEFAULT_MESSAGE);
    }

    public NotAllowValueException(String s) {
        super(s);
    }
}