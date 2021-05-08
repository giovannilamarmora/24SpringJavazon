package org.generation.italy.javazon.SpringJavazon.dao;

import org.generation.italy.javazon.SpringJavazon.model.Categoria;
import org.generation.italy.javazon.SpringJavazon.model.Prodotto;

import java.util.List;

public interface IDaoCategorie {



    /**
     * Lista delle categorie
     * @return una lista di categorie
     */
    List<Categoria> categorie();

    /**
     * Categoria singola
     * @param id parametro di ricerca
     * @return Una singola categoria
     */
    Categoria categoria(int id);

    /**
     * In principio era void, ma siccome vorrei effettuare dei controlli più specifici, utilizzerò un boolean
     * @param categoria Aggiunta di una categoria
     */
    boolean add(Categoria categoria);
    // attenzione che bisogna cancellare i prodotti legati alla
    // categoria

    /**
     * In principio era void, ma siccome vorrei effettuare dei controlli più specifici, utilizzerò un boolean
     * @param id Parametro per la cancellazione
     * @return True se la cancellazione è avvenuta, false se non lo è
     */
    boolean delete(int id);

    /**
     * In principio era void, ma siccome vorrei effettuare dei controlli più specifici, utilizzerò un boolean
     * @param categoria Oggetto per la modifica
     * @return True se la modifica è avvenuta, false se non lo è
     */
    boolean update(Categoria categoria);
}
