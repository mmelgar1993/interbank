package com.pe.interbank.service;

import com.pe.interbank.entity.request.ProductoRequest;
import com.pe.interbank.entity.response.ProductoResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProductoService {
    Flux<ProductoResponse> createProducto(List<ProductoRequest> productoRequest);

    Flux<ProductoResponse> finAll();

    Mono<ProductoResponse> updateProducto(String id, ProductoRequest productoRequest);

    Mono<ProductoResponse> findById(String id);
}
