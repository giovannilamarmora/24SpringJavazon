package org.generation.italy.javazon.SpringJavazon.model;

import org.generation.italy.javazon.SpringJavazon.utils.IMappablePro;

public class Prodotto implements IMappablePro {

    private int id;
    private String nome;
    private String descrizione;
    private double prezzo;
    private int disponibilita;
    private Categoria categoria;
    private String image;

    public Prodotto(int id, String nome, String descrizione, double prezzo, int disponibilita, Categoria categoria, String image) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.disponibilita = disponibilita;
        this.categoria = categoria;
        this.image = image;
    }
    public Prodotto(int id, String nome, String descrizione, double prezzo, int disponibilita, String image) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.disponibilita = disponibilita;
        this.image = image;
    }

    public Prodotto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public int getDisponibilita() {
        return disponibilita;
    }

    public void setDisponibilita(int disponibilita) {
        this.disponibilita = disponibilita;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
