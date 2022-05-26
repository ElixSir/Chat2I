package com.example.chat_2022_eleves;

import java.util.ArrayList;
import java.util.List;

public class ListMessages {
    ArrayList<Message> messages;

    public ListMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    // {"id":"23","active":"0","theme":"test"

    @Override
    public String toString() {
        return "ListMessages{" +
                "messages='" + messages + '\'' +
                '}';
    }

    public int getSize() {
        return messages.size();
    }


    public List<Message> getMessages() {
        return messages;
    }
}
