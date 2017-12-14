package com.andreas.chat.common;

public class Message {
    private String username;
    private String text;

    public Message() {
    }

    public Message(String stringRepresentation) {
        String[] tokens = stringRepresentation.trim().split(Constants.DELIMITER);
        if (tokens.length > 0)
            this.username = tokens[0];
        if (tokens.length > 1)
            this.text = tokens[1];
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return username + Constants.DELIMITER + text;
    }

}
