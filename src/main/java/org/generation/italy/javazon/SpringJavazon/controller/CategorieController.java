package org.generation.italy.javazon.SpringJavazon.controller;

import org.generation.italy.javazon.SpringJavazon.dao.IDaoCategorie;
import org.generation.italy.javazon.SpringJavazon.model.Categoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/categorie")
public class CategorieController {

    @Autowired
    private IDaoCategorie categorie;

    @GetMapping
    public List<Categoria> get() {
        return categorie.categorie();
    }

    @GetMapping("{id}")
    public Categoria get(@PathVariable int id, HttpServletResponse response) throws IOException {
        Categoria singolo = null;
        String error = "";
        try {
            singolo = categorie.categoria(id);
        } catch (NullPointerException e){
            System.out.println("Id Non Presente, NullPointerException Gestito Correttamente");
            error = "{\"messaggio\" : \"ID inserito non presente\" }";
            response.getWriter().append(error);
        }
        response.setContentType("application/json");
        return singolo;
    }

    @PostMapping
    public void post(@RequestBody Categoria categoria, HttpServletResponse response) throws IOException {
        String post = "";
        if (categorie.add(categoria)){
            post = "{ \"messaggio\" : \"Detto, Fatto\" }";
        } else {
            post = "{\"messaggio\" : \"Metodo Post non Funzionante\" }";
        }
        response.setContentType("application/json");
        response.getWriter().append(post);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable int id, HttpServletResponse response) throws IOException {
        String delete = "";
        if (categorie.delete(id)){
            delete = "{ \"messaggio\" : \"Detto, Fatto\" }";
        } else {
            delete = "{\"messaggio\" : \"Metodo Delete non Funzionante\" }";
        }
        response.setContentType("application/json");
        response.getWriter().append(delete);
    }

    @PutMapping
    public void put(@RequestBody Categoria categoria, HttpServletResponse response) throws IOException {
        String put = "";
        if (categorie.update(categoria)) {
            put = "{ \"messaggio\" : \"Detto, Fatto\" }";
        } else {
            put = "{\"messaggio\" : \"Metodo Put non Funzionante\" }";
        }
        response.setContentType("application/json");
        response.getWriter().append(put);
    }
}
