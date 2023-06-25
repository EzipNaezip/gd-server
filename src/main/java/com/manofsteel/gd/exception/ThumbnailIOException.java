package com.manofsteel.gd.exception;

import java.io.IOException;

public class ThumbnailIOException extends IOException {
    private static final String DEFAULT_MESSAGE = "Thumbnail IO operation failed!";

    public ThumbnailIOException() {
        super(DEFAULT_MESSAGE);
    }

    public ThumbnailIOException(String message) {
        super(message);
    }

    public ThumbnailIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
