package com.pe.interbank.service.impl;

import com.pe.interbank.adapter.PedidoAdapter;
import com.pe.interbank.entity.model.Pedido;
import com.pe.interbank.entity.request.PedidoRequest;
import com.pe.interbank.entity.response.PedidoResumenResponse;
import com.pe.interbank.repository.PedidoRepository;
import com.pe.interbank.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoAdapter pedidoAdapter;


    @Override
    public Mono<PedidoResumenResponse> procesarPedido(List<PedidoRequest> pedidoRequests) {
        return pedidoAdapter.updateStockProductos(pedidoRequests)
                .flatMap(respuestas -> {
                    List<Pedido> pedidos = respuestas.getPedidos().stream()
                            .map(pedidoResponse ->
                                    Pedido.builder()
                                            .fecha(pedidoResponse.getDate())
                                            .estado(pedidoResponse.getState())
                                            .total(pedidoAdapter.calcularTotalConDescuento(respuestas.getPedidos()))
                                            .build())

                            .collect(Collectors.toList());
                    return pedidoRepository.saveAll(pedidos).collectList().thenReturn(respuestas);
                });
    }
}
