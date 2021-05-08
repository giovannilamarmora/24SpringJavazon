package org.generation.italy.javazon.SpringJavazon.dao;

import org.generation.italy.javazon.SpringJavazon.model.Categoria;
import org.generation.italy.javazon.SpringJavazon.model.Prodotto;

import java.util.List;

public interface IDaoProdotti {

    /**
     * Lista di tutti i prodotti
     * @return Una lista dei prodotti
     */
    List<Prodotto> prodotti();

    /**
     * Singolo prodotto
     * @param id Parametro esterno
     * @return Un singolo prodotto secondo id
     */
    Prodotto prodotto(int id);

    /**
     * In principio era void, modificato in boolean per gestione migliore di un errore
     * @param p Aggiunta di un prodotto
     */
    boolean add(Prodotto p);

    /**
     * In principio era void, modificato in boolean per gestione migliore di un errore
     * @param id Parametro per la cancellazione
     */
    boolean delete(int id);

    /**
     * In principio era void, modificato in boolean per gestione migliore di un errore
     * @param p Oggetto da modificare, ricorda di inserire l'id.
     */
    boolean update(Prodotto p);
}
