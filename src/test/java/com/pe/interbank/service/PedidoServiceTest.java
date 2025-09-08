package com.pe.interbank.service;

import com.pe.interbank.adapter.PedidoAdapter;
import com.pe.interbank.entity.model.Pedido;
import com.pe.interbank.entity.request.PedidoRequest;
import com.pe.interbank.entity.response.PedidoResponse;
import com.pe.interbank.entity.response.PedidoResumenResponse;
import com.pe.interbank.repository.PedidoRepository;
import com.pe.interbank.service.impl.PedidoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {
    @Mock
    private PedidoAdapter pedidoAdapter;

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoServiceImpl pedidoService;


    @Test
    void procesarPedido_deberiaRetornarResumenConPedidosYGuardarEnBD() {
        // Arrange
        List<PedidoRequest> pedidoRequests = List.of(
                new PedidoRequest(1L, 2),
                new PedidoRequest(2L, 3)
        );

        List<PedidoResponse> respuestas = List.of(
                new PedidoResponse(1L, 500.0, LocalDate.now(), "ACTIVO"),
                new PedidoResponse(2L, 600.0, LocalDate.now(), "ACTIVO")
        );

        PedidoResumenResponse resumen = new PedidoResumenResponse(respuestas, 990.0);

        List<Pedido> pedidos = respuestas.stream()
                .map(r -> Pedido.builder()
                        .fecha(r.getDate())
                        .estado(r.getState())
                        .total(990.0) // total con descuento aplicado
                        .build())
                .collect(Collectors.toList());

        // Mocking
        Mockito.when(pedidoAdapter.updateStockProductos(pedidoRequests))
                .thenReturn(Mono.just(resumen));

        Mockito.when(pedidoRepository.saveAll(Mockito.anyList()))
                .thenReturn(Flux.fromIterable(pedidos));


        // Act & Assert
        StepVerifier.create(pedidoService.procesarPedido(pedidoRequests))
                .expectNextMatches(r -> r.getPedidos().size() == 2 && r.getTotalGlobal() == 990.0)
                .verifyComplete();
    }
}
