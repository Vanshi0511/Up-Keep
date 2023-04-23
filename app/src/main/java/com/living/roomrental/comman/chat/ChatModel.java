package com.living.roomrental.comman.chat;

public class ChatModel {

    private String time;
    private String message;
    private int type;

    public ChatModel(String time, String message, int type) {
        this.time = time;
        this.message = message;
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
