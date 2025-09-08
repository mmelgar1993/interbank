package com.pe.interbank.service;

import com.pe.interbank.entity.request.PedidoRequest;
import com.pe.interbank.entity.response.PedidoResumenResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PedidoService {
    Mono<PedidoResumenResponse> procesarPedido(List<PedidoRequest> pedidoRequests);
}
