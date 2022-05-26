package com.example.chat_2022_eleves;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Promo {

    String promo;
    @SerializedName("enseignants")
    ArrayList<Enseignant> profs;

    @Override
    public String toString() {
        return "Promo{" +
                "promo='" + promo + '\'' +
                ", profs=" + profs +
                '}';
    }
}
