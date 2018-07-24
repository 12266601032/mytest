package com.example.jsr303demo.model;

import java.io.Serializable;

public class ServiceSCMResult<T> extends ServiceResult<T, String> implements Serializable {


    public ServiceSCMResult() {
    }

    public ServiceSCMResult(T data, CodeMessage<String> message) {
        super(data, message);
    }

    public static <T> ServiceSCMResult<T> success(T data) {
        return new ServiceSCMResult<>(data, StringCodeMessage.success());
    }

    public static <T> ServiceSCMResult<T> success(T data, String sucessCode, String successMessage) {
        return new ServiceSCMResult<>(data, StringCodeMessage.success(sucessCode, successMessage));
    }

    public static <T> ServiceSCMResult<T> success(T data, String successMessage) {
        return new ServiceSCMResult<>(data, StringCodeMessage.success(successMessage));
    }

    public static <T> ServiceSCMResult<T> success(T data, CodeMessage<String> codeMessage) {
        return new ServiceSCMResult<>(data, codeMessage);
    }

    public static <T> ServiceSCMResult<T> error(String errorCode, String errorMessage) {
        return new ServiceSCMResult<>(null, StringCodeMessage.error(errorCode, errorMessage));
    }

    public static <T> ServiceSCMResult<T> error(CodeMessage<String> codeMessage) {
        return new ServiceSCMResult<>(null, codeMessage);
    }

}

