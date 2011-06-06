package com.donkfish.core.client.model;

import java.io.Serializable;

public class Command implements Serializable {


    private String text;
    public static final Integer TITLECASE = 1;
    public static final Integer PROPERCASE = 2;
    private Integer method;

    public Command(Integer method, String text) {
        this.method = method;
        this.text = text;
    }

    public Command() {
    }

    public String getText() {
        return text;
    }

    public Integer getMethod() {
        return method;
    }
}
