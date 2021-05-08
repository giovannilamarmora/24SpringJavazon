package org.generation.italy.javazon.SpringJavazon.dao;

import org.generation.italy.javazon.SpringJavazon.model.Categoria;
import org.generation.italy.javazon.SpringJavazon.model.Prodotto;
import org.generation.italy.javazon.SpringJavazon.utils.BasicDao;
import org.generation.italy.javazon.SpringJavazon.utils.IMappablePro;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class DaoProdotti extends BasicDao implements IDaoProdotti {

    public DaoProdotti(
            @Value("${db.address}") String dbAddress,
            @Value("${db.username}") String user,
            @Value("${db.password}") String password) {
        super(dbAddress, user, password);
    }

    /**
     * Lista di tutti i prodotti
     *
     * @return Una lista dei prodotti
     */
    @Override
    public List<Prodotto> prodotti() {
        List<Prodotto> lista = new ArrayList<>();

        List<Map<String, String>> maps = getAll("SELECT * FROM prodotti");

        for (Map<String, String> map : maps) {
            lista.add(IMappablePro.fromMap(Prodotto.class, map));
        }
        return lista;
    }

    /**
     * Singolo prodotto
     *
     * @param id Parametro esterno
     * @return Un singolo prodotto secondo id
     */
    @Override
    public Prodotto prodotto(int id) {
        Prodotto singolo = null;
      //  Map<String, String> map = getOne("SELECT * FROM prodotti INNER JOIN categorie ON categorie.id = idcategoria WHERE prodotti.id = ?", id);
        Map<String, String> map = getOne("SELECT * FROM prodotti WHERE id = ?", id);
        Map<String, String> maps = getOne("SELECT * FROM categorie WHERE id = ?", map.get("idcategoria"));
        if (map != null){
            singolo = IMappablePro.fromMap(Prodotto.class, map);
            if (map.get("idcategoria") != null) {
                singolo.setCategoria(IMappablePro.fromMap(Categoria.class, maps));

            }
        }
        return singolo;
    }

    /**
     * In principio era void, modificato in boolean per gestione migliore di un errore
     *
     * @param p Aggiunta di un prodotto
     */
    @Override
    public boolean add(Prodotto p) {
        if (p != null) {
            execute("INSERT into prodotti (nome, descrizione, prezzo, disponibilita, idcategoria, image) VALUES (?, ?, ?, ?, ?, ?)",
                    p.getNome(),
                    p.getDescrizione(),
                    p.getPrezzo(),
                    p.getDisponibilita(),
                    p.getCategoria().getId(),
                    p.getImage());
            return true;
        }
        return false;
    }

    /**
     * In principio era void, modificato in boolean per gestione migliore di un errore
     *
     * @param id Parametro per la cancellazione
     */
    @Override
    public boolean delete(int id) {
        if (id != 0){
            execute("DELETE from prodotti WHERE id = ?", id);
            return true;
        }
        return false;
    }

    /**
     * In principio era void, modificato in boolean per gestione migliore di un errore
     *
     * @param p Oggetto da modificare, ricorda di inserire l'id.
     */
    @Override
    public boolean update(Prodotto p) {
        if (p != null) {
            execute("UPDATE prodotti SET nome = ?, descrizione = ?, prezzo = ?, disponibilita = ?, idcategoria = ? WHERE id = ?",
                    p.getNome(),
                    p.getDescrizione(),
                    p.getPrezzo(),
                    p.getDisponibilita(),
                    p.getCategoria().getId(),
                    p.getId());
            return true;
        }
        return false;
    }
}
