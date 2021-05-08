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
public class DaoCategorie extends BasicDao implements IDaoCategorie {

    public DaoCategorie(
            @Value("${db.address}") String dbAddress,
            @Value("${db.username}") String user,
            @Value("${db.password}") String password) {
        super(dbAddress, user, password);
    }

    /**
     * Lista delle categorie
     *
     * @return una lista di categorie
     */
    @Override
    public List<Categoria> categorie() {
        List<Categoria> lista = new ArrayList<>();
        List<Map<String, String>> maps = getAll("SElECT * FROM categorie");

        for (Map<String, String> map : maps) {
            lista.add(IMappablePro.fromMap(Categoria.class, map));
        }
        return lista;
    }

    /**
     * Categoria singola
     *
     * @param id parametro di ricerca
     * @return Una singola categoria
     */
    @Override
    public Categoria categoria(int id) {
        return IMappablePro.fromMap(Categoria.class, getOne("SELECT * FROM categorie WHERE id = ?", id));
    }

    /**
     * In principio era void, ma siccome vorrei effettuare dei controlli più specifici, utilizzerò un boolean
     *
     * @param categoria Aggiunta di una categoria
     */
    @Override
    public boolean add(Categoria categoria) {
        if (categoria != null){
            execute("INSERT into categorie (nome, iva) VALUES (?, ?)",
                    categoria.getNome(),
                    categoria.getIva());
            return true;
        }
        return false;
    }

    /**
     * In principio era void, ma siccome vorrei effettuare dei controlli più specifici, utilizzerò un boolean
     *
     * @param id Parametro per la cancellazione
     * @return True se la cancellazione è avvenuta, false se non lo è
     */
    @Override
    public boolean delete(int id) {
        if (id != 0){
            execute("DELETE from categorie WHERE id = ?", id);
            return true;
        }
        return false;
    }

    /**
     * In principio era void, ma siccome vorrei effettuare dei controlli più specifici, utilizzerò un boolean
     *
     * @param categoria Oggetto per la modifica
     * @return True se la modifica è avvenuta, false se non lo è
     */
    @Override
    public boolean update(Categoria categoria) {
        if (categoria != null){
            execute("UPDATE categorie SET nome = ?, iva = ? WHERE id = ?",
                    categoria.getNome(),
                    categoria.getIva(),
                    categoria.getId());
            return true;
        }
        return false;
    }
}
