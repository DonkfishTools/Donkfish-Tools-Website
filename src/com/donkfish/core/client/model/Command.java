package com.donkfish.core.client.model;

import java.io.Serializable;

public class Command implements Serializable {


        private Integer method;
    private String text;
    public static final Integer TITLECASE = 1;
    public static final Integer PROPERCASE = 2;
    public static final int ENCODE_HTML = 3;
    public static final int ENCODE_XML = 4;
    public static final int DECODE_HTML = 5;
    public static final int ENCODE_BASE64 = 6;
    public static final int DECODE_BASE64 = 7;
    public static final int DECODE_XML = 8;
    public static final Integer ENCODE_JAVA = 9;
    public static final Integer DECODE_JAVA = 10;
    public static final Integer ENCODE_JS = 11;
    public static final Integer DECODE_JS = 12;
    public static final Integer ENCODE_CSV = 13;
    public static final Integer DECODE_CSV = 14;

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
