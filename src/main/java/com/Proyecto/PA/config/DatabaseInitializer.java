package com.Proyecto.PA.config;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class DatabaseInitializer {

    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void init() {
        if (!mongoTemplate.collectionExists("usuarios")) {
            mongoTemplate.createCollection("usuarios");
        }
        if (!mongoTemplate.collectionExists("productos")) {
            mongoTemplate.createCollection("productos");
        }
        if (!mongoTemplate.collectionExists("compras")) {
            mongoTemplate.createCollection("compras");
        }
        insertarComprasEjemplo();
    }

    private void insertarComprasEjemplo() {
        String[] productos = {"abrigo-01", "camiseta-01", "pantalon-01"};
        for (int i = 0; i < 100; i++) {
            String usuarioId = String.format("682bc1a8b7831d63fdbf%04d", 1667 + i);
            String productoId = productos[i % productos.length];
            int cantidad = 1 + (i % 10); // Cantidad entre 1 y 5
            int total = cantidad * 1000;

            Document compra = new Document();
            compra.put("usuarioId", usuarioId);

            List<Document> productosList = new ArrayList<>();
            Document producto = new Document();
            producto.put("productoId", productoId);
            producto.put("cantidad", cantidad);
            productosList.add(producto);

            compra.put("productos", productosList);
            compra.put("total", total);
            compra.put("fecha", Date.from(Instant.now()));
            compra.put("_class", "com.Proyecto.PA.model.Compra");

            mongoTemplate.insert(compra, "compras");
        }
    }
}