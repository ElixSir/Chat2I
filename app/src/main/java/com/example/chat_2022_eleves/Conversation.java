package com.example.chat_2022_eleves;

import android.content.Intent;

public class Conversation {
    String id;
    String active;
    String theme;


    // {"id":"23","active":"0","theme":"test"

    @Override
    public String toString() {
        return "Conversation{" +
                "id='" + id + '\'' +
                ", active='" + active + '\'' +
                ", theme='" + theme + '\'' +
                '}';
    }

    public boolean getActive() {
        return active.equals("1");
    }

    public String getTheme() {
        return theme;
    }

    public int getId() {
        return Integer.parseInt(id);
    }
}
