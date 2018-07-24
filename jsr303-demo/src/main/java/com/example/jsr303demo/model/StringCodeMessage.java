package com.example.jsr303demo.model;

import java.io.Serializable;

public class StringCodeMessage extends AnyCodeMessage<String> implements Serializable {

    public static final StringCodeMessage SUCCESS_CM = new StringCodeMessage("00000000", "SUCCESS", true);
    public static final StringCodeMessage ILLEGAL_PARAM_CM = new StringCodeMessage("10000000", "ILLEGAL PARAMETER", false);

    public StringCodeMessage() {
    }

    StringCodeMessage(String code, String message, boolean success) {
        super(code, message, success);
    }

    public static StringCodeMessage success(String successCode, String successMessage) {
        return new StringCodeMessage(successCode, successMessage, true);
    }

    public static StringCodeMessage success(String successMessage) {
        return new StringCodeMessage(SUCCESS_CM.code, successMessage, true);
    }

    public static StringCodeMessage success() {
        return SUCCESS_CM;
    }

    public static StringCodeMessage error(String errorCode, String errorMessage) {
        return new StringCodeMessage(errorCode, errorMessage, false);
    }

    public static StringCodeMessage newInstance(String code, String message, boolean success) {
        return new StringCodeMessage(code, message, success);
    }

}
