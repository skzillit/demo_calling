package com.example.demovideocalling.lib;

public class ProtooException extends Exception {
    private long error;
    private String errorReason;

    public ProtooException(long error, String errorReason) {
        this.error = error;
        this.errorReason = errorReason;
    }
}