package com.example.jsr303demo.model;

import java.io.Serializable;

public abstract class AnyCodeMessage<C> implements CodeMessage<C>, Serializable {

    protected C code;
    protected String message;
    protected boolean success;

    public AnyCodeMessage() {
    }

    protected AnyCodeMessage(C code, String message, boolean success) {
        this.code = code;
        this.message = message;
        this.success = success;
    }

    @Override
    public C getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public boolean isSuccess() {
        return this.success;
    }
}
