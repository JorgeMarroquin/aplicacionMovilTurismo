package com.example.turismo.models;

public class Message {
    private String msg;
    private boolean result;

    public Message() {
    }

    public Message(String msg) {
        this.msg = msg;
    }

    public Message(boolean result) {
        this.result = result;
    }

    public Message(String msg, boolean result) {
        this.msg = msg;
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
