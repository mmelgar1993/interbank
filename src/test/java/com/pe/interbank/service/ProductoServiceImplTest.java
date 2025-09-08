package com.pe.interbank.service;

import com.pe.interbank.entity.model.Producto;
import com.pe.interbank.entity.request.ProductoRequest;
import com.pe.interbank.repository.ProductoRepository;
import com.pe.interbank.service.impl.ProductoServieImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceImplTest {
    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServieImpl productoService;

    @Test
    void createProducto_deberiaGuardarYRetornarProductoResponse() {
        List<ProductoRequest> requests = List.of(
                new ProductoRequest("Laptop", 2500.0, 10),
                new ProductoRequest("Mouse", 15.0, 30)
        );

        List<Producto> productos = requests.stream()
                .map(r -> Producto.builder()
                        .nombre(r.getName())
                        .precio(r.getPrice())
                        .stock(r.getStock())
                        .build())
                .collect(Collectors.toList());

        List<Producto> productosConId = List.of(
                Producto.builder().id(1L).nombre("Laptop").precio(2500.0).stock(10).build(),
                Producto.builder().id(2L).nombre("Mouse").precio(15.0).stock(30).build()
        );

        Mockito.when(productoRepository.saveAll(Mockito.anyList()))
                .thenReturn(Flux.fromIterable(productosConId));

        StepVerifier.create(productoService.createProducto(requests))
                .expectNextMatches(p -> p.getName().equals("Laptop") && p.getPrice() == 2500.0)
                .expectNextMatches(p -> p.getName().equals("Mouse") && p.getStock() == 30)
                .verifyComplete();
    }

    @Test
    void findAll_deberiaRetornarTodosLosProductos() {
        List<Producto> productos = List.of(
                Producto.builder().id(1L).nombre("Laptop").precio(2500.0).stock(10).build(),
                Producto.builder().id(2L).nombre("Mouse").precio(15.0).stock(30).build()
        );

        Mockito.when(productoRepository.findAll())
                .thenReturn(Flux.fromIterable(productos));

        StepVerifier.create(productoService.finAll())
                .expectNextMatches(p -> p.getId() == 1L && p.getName().equals("Laptop"))
                .expectNextMatches(p -> p.getId() == 2L && p.getName().equals("Mouse"))
                .verifyComplete();
    }

    @Test
    void updateProducto_deberiaActualizarYSumarStock() {
        ProductoRequest request = new ProductoRequest("Laptop", 2500.0, 5);

        Producto existente = Producto.builder()
                .id(1L).nombre("Laptop").precio(2500.0).stock(10).build();

        Producto actualizado = Producto.builder()
                .id(1L).nombre("Laptop").precio(2500.0).stock(15).build();

        Mockito.when(productoRepository.findById(1L)).thenReturn(Mono.just(existente));
        Mockito.when(productoRepository.save(Mockito.any())).thenReturn(Mono.just(actualizado));

        StepVerifier.create(productoService.updateProducto("1", request))
                .expectNextMatches(p -> p.getStock() == 15)
                .verifyComplete();
    }

    @Test
    void findById_deberiaRetornarProductoResponse() {
        Producto producto = Producto.builder()
                .id(1L).nombre("Laptop").precio(2500.0).stock(10).build();

        Mockito.when(productoRepository.findById(1L)).thenReturn(Mono.just(producto));

        StepVerifier.create(productoService.findById("1"))
                .expectNextMatches(p -> p.getId() == 1L && p.getName().equals("Laptop"))
                .verifyComplete();
    }


}
