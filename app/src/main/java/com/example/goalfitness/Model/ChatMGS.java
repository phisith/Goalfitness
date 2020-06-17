package com.example.goalfitness.Model;

public class ChatMGS {

    private String sender;
    private String receiver;
    private String message;
    private boolean readed;

    public ChatMGS(String sender, String receiver, String message, boolean readed) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.readed = readed;
    }

    public ChatMGS() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isReaded() {
        return readed;
    }

    public void setReaded(boolean readed) {
        this.readed = readed;
    }
}
