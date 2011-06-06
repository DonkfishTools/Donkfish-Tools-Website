package com.donkfish.core.client.model;

import java.io.Serializable;

public class CommandResult implements Serializable {
    private String payLoad;

    public CommandResult(String payLoad) {
        this.payLoad = payLoad;
    }

    public CommandResult() {
    }

    public String getPayLoad() {
        return payLoad;
    }
}
