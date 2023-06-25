package com.manofsteel.gd.exception;

public class ThumbnailCreationException extends Exception {
    private static final String DEFAULT_MESSAGE = "Thumbnail creation failed!";

    public ThumbnailCreationException() {
        super(DEFAULT_MESSAGE);
    }

    public ThumbnailCreationException(String message) {
        super(message);
    }

    public ThumbnailCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
