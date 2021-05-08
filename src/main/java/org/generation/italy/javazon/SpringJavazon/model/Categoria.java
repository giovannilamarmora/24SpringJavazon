package org.generation.italy.javazon.SpringJavazon.model;

import org.generation.italy.javazon.SpringJavazon.utils.IMappablePro;

public class Categoria implements IMappablePro {

    private int id;
    private String nome;
    private int iva;

    public Categoria(int id, String nome, int iva) {
        this.id = id;
        this.nome = nome;
        this.iva = iva;
    }

    public Categoria() {
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

    public int getIva() {
        return iva;
    }

    public void setIva(int iva) {
        this.iva = iva;
    }
}
