package com.kitchenpointers.domain;

public class ErrorInfo {
    public final String url;
    public final String message;
    public final String ex;

    public ErrorInfo(String url, String message, Exception ex) {
        this.url = url;
        this.message = message;
        this.ex = ex.getLocalizedMessage();
    }
}
