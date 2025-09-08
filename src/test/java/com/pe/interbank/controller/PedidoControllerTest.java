package com.pe.interbank.controller;

import com.pe.interbank.entity.request.PedidoRequest;
import com.pe.interbank.entity.response.PedidoResponse;
import com.pe.interbank.entity.response.PedidoResumenResponse;
import com.pe.interbank.service.PedidoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@WebFluxTest(controllers = PedidoController.class)
public class PedidoControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PedidoService pedidoService;

    @Test
    void procesarPedidos_deberiaRetornar200ConResumen() {
        List<PedidoRequest> requests = List.of(
                new PedidoRequest(1L, 2),
                new PedidoRequest(2L, 3)
        );

        List<PedidoResponse> responses = List.of(
                new PedidoResponse(1L, 500.0, LocalDate.now(), "ACTIVO"),
                new PedidoResponse(2L, 600.0, LocalDate.now(), "ACTIVO")
        );

        PedidoResumenResponse resumen = new PedidoResumenResponse(responses, 990.0);

        Mockito.when(pedidoService.procesarPedido(Mockito.any()))
                .thenReturn(Mono.just(resumen));

        webTestClient.post()
                .uri("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requests)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PedidoResumenResponse.class)
                .isEqualTo(resumen);
    }


}
