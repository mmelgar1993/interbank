package com.pe.interbank.controller;


import com.pe.interbank.entity.request.ProductoRequest;
import com.pe.interbank.entity.response.ProductoResponse;
import com.pe.interbank.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Productos", description = "Operaciones relacionadas con productos")
@Validated
@RestController
@RequestMapping("/api")
public class ProductoController {

    @Autowired
    private ProductoService productoService;


    @Operation(summary = "Crear productos", description = "Registra una lista de productos")
    @PostMapping("/productos")
    public Mono<ResponseEntity<List<ProductoResponse>>> crearProducto(@Valid @RequestBody List<ProductoRequest> productos) {
        if (productos == null || productos.isEmpty()) {
            return Mono.just(ResponseEntity.badRequest().build());
        }

        return productoService.createProducto(productos)
                .collectList()
                .map(lista -> ResponseEntity.status(HttpStatus.CREATED).body(lista));
    }


    @Operation(summary = "Listar productos")
    @GetMapping("/productos")
    public ResponseEntity<?> listarProductos() {
        return ResponseEntity.ok(productoService.finAll());
    }

    @Operation(summary = "Actualiza productos", description = "Actualiza un producto")
    @PutMapping("/productos/{id}")
    public Mono<ResponseEntity<ProductoResponse>> actualizarProducto(
            @PathVariable String id,
            @Valid @RequestBody ProductoRequest productoRequest) {

        return productoService.updateProducto(id, productoRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
