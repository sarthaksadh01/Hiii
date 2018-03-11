package com.cool.sarthak.hiii;

/**
 * Created by sarthak on 18/01/18.
 */

public class Messages {
    private String message,type,from;

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {

        return from;
    }

    public Messages() {

    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {

        return type;
    }

    public void setMessage(String message) {

        this.message = message;
    }

    public String getMessage() {

        return message;
    }

    public Messages(String message) {

        this.message = message;
    }
}
