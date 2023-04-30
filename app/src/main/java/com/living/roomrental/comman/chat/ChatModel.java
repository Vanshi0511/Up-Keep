package com.living.roomrental.comman.chat;

public class ChatModel {

    private String time;
    private String message;

    private String senderId ;

    public ChatModel() {
    }

    public ChatModel(String time, String message, String senderId) {
        this.time = time;
        this.message = message;
        this.senderId = senderId;
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

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
