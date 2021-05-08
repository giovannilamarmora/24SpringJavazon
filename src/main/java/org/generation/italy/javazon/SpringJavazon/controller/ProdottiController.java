package org.generation.italy.javazon.SpringJavazon.controller;

import org.generation.italy.javazon.SpringJavazon.dao.IDaoProdotti;
import org.generation.italy.javazon.SpringJavazon.model.Prodotto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/prodotti")
public class ProdottiController {

    @Autowired
    private IDaoProdotti prodotti;

    @GetMapping
    public List<Prodotto> get() { return prodotti.prodotti(); }

    @GetMapping("{id}")
    public Prodotto get(@PathVariable int id, HttpServletResponse response) throws IOException {
        Prodotto singolo = null;
        String error = "";
        try {
            singolo = prodotti.prodotto(id);
        } catch (NullPointerException e){
            System.out.println("ID Non presente, NullPointerException Gestito");
            error = "{\"messaggio\" : \"ID inserito non presente\" }";
            response.getWriter().append(error);
        }
        response.setContentType("application/json");
        return singolo;
    }

    @PostMapping
    public void post(@RequestBody Prodotto prodotto, HttpServletResponse response) throws IOException {

        String post = "";
        if (prodotti.add(prodotto)){
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
        if (prodotti.delete(id)) {
            delete = "{ \"messaggio\" : \"Detto, Fatto\" }";
        } else {
            delete = "{\"messaggio\" : \"Metodo Delete non Funzionante\" }";
        }
        response.setContentType("application/json");
        response.getWriter().append(delete);
    }

    @PutMapping
    public void put(@RequestBody Prodotto prodotto, HttpServletResponse response) throws IOException {
        String put = "";
        if (prodotti.update(prodotto)){
            put = "{ \"messaggio\" : \"Detto, Fatto\" }";
        } else {
            put = "{\"messaggio\" : \"Metodo Put non Funzionante\" }";
        }
        response.setContentType("application/json");
        response.getWriter().append(put);
    }
}
