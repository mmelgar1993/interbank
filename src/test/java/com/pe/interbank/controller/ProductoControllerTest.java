package com.pe.interbank.controller;

import com.pe.interbank.entity.request.ProductoRequest;
import com.pe.interbank.entity.response.ProductoResponse;
import com.pe.interbank.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@WebFluxTest(controllers = ProductoController.class)
public class ProductoControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ProductoService productoService;

    @Test
    void crearProducto_deberiaRetornar201ConListaDeProductos() {
        List<ProductoRequest> requests = List.of(
                new ProductoRequest("Laptop", 2500.0, 10),
                new ProductoRequest("Mouse", 15.0, 30)
        );

        List<ProductoResponse> responses = List.of(
                new ProductoResponse(1L, "Laptop", 2500.0, 10),
                new ProductoResponse(2L, "Mouse", 15.0, 30)
        );

        Mockito.when(productoService.createProducto(Mockito.any()))
                .thenReturn(Flux.fromIterable(responses));

        webTestClient.post()
                .uri("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requests)
                .exchange()
                .expectStatus().isCreated()
                .expectBodyList(ProductoResponse.class)
                .hasSize(2)
                .contains(responses.get(0), responses.get(1));
    }

    @Test
    void listarProductos_deberiaRetornarTodosLosProductos() {
        List<ProductoResponse> responses = List.of(
                new ProductoResponse(1L, "Laptop", 2500.0, 10),
                new ProductoResponse(2L, "Mouse", 15.0, 30)
        );

        Mockito.when(productoService.finAll())
                .thenReturn(Flux.fromIterable(responses));

        webTestClient.get()
                .uri("/api/productos")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductoResponse.class)
                .hasSize(2)
                .contains(responses.get(0), responses.get(1));
    }

    @Test
    void actualizarProducto_deberiaRetornarProductoActualizado() {
        ProductoRequest request = new ProductoRequest("Laptop", 2500.0, 5);
        ProductoResponse response = new ProductoResponse(1L, "Laptop", 2500.0, 15);

        Mockito.when(productoService.updateProducto(Mockito.any(), Mockito.any()))
                .thenReturn(Mono.just(response));

        webTestClient.put()
                .uri("/api/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductoResponse.class)
                .isEqualTo(response);
    }


}
