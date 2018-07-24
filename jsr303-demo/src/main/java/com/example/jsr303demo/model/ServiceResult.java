package com.example.jsr303demo.model;

import java.io.Serializable;

/**
 * @see CodeMessage
 */
public abstract class ServiceResult<T, C> implements Serializable {


    protected T data;
    protected C code;
    protected boolean success;
    protected String message;

    public ServiceResult() {
    }

    protected ServiceResult(T data, CodeMessage<C> codeMessage) {
        this.data = data;
        this.message = codeMessage.getMessage();
        this.code = codeMessage.getCode();
        this.success = codeMessage.isSuccess();
    }

    public T getData() {
        return data;
    }

    public C getCode() {
        return code;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

}
