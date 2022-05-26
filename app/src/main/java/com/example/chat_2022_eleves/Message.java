package com.example.chat_2022_eleves;

public class Message {
    String id;
    String contenu;
    String auteur;
    String couleur;


    //{"id":"145","contenu":"test","auteur":"tom","couleur":"blue"}


    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", contenu='" + contenu + '\'' +
                ", auteur='" + auteur + '\'' +
                ", couleur='" + couleur + '\'' +
                '}';
    }

    public String getContenu() {
        return contenu;
    }

    public String getAuteur() {
        return auteur;
    }

    public String getCouleur() {
        return couleur;
    }

    public int getId() {
        return Integer.parseInt(id);
    }
}
