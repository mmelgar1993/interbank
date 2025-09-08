package com.pe.interbank.service.impl;

import com.pe.interbank.entity.model.Producto;
import com.pe.interbank.entity.request.ProductoRequest;
import com.pe.interbank.entity.response.ProductoResponse;
import com.pe.interbank.repository.ProductoRepository;
import com.pe.interbank.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoServieImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public Flux<ProductoResponse> createProducto(List<ProductoRequest> productoRequest) {
        List<Producto> producto = productoRequest.stream()
                .map(p -> Producto.builder()
                        .nombre(p.getName())
                        .precio(p.getPrice())
                        .stock(p.getStock())
                        .build()).collect(Collectors.toList());

        return productoRepository.saveAll(producto)
                .map(saved -> ProductoResponse.builder()
                        .id(saved.getId())
                        .name(saved.getNombre())
                        .price(saved.getPrecio())
                        .stock(saved.getStock())
                        .build()
                );

    }

    @Override
    public Flux<ProductoResponse> finAll() {
        return productoRepository.findAll()
                .map(producto -> ProductoResponse.builder()
                        .id(producto.getId())
                        .name(producto.getNombre())
                        .price(producto.getPrecio())
                        .stock(producto.getStock())
                        .build()
                );
    }

    @Override
    public Mono<ProductoResponse> updateProducto(String id, ProductoRequest productoRequest) {


        return productoRepository.findById(Long.parseLong(id))
                .flatMap(existing -> {
                    existing.setNombre(productoRequest.getName());
                    existing.setPrecio(productoRequest.getPrice());
                    existing.setStock(existing.getStock() + productoRequest.getStock());
                    return productoRepository.save(existing);
                })
                .map(updated -> new ProductoResponse(
                        updated.getId(),
                        updated.getNombre(),
                        updated.getPrecio(),
                        updated.getStock()
                ));
    }

    @Override
    public Mono<ProductoResponse> findById(String id) {
        return productoRepository.findById(Long.parseLong(id))
                .map(producto -> ProductoResponse.builder()
                        .id(producto.getId())
                        .name(producto.getNombre())
                        .price(producto.getPrecio())
                        .stock(producto.getStock())
                        .build()
                );
    }
}
