package com.pe.interbank.controller;


import com.pe.interbank.entity.request.PedidoRequest;
import com.pe.interbank.entity.response.PedidoResumenResponse;
import com.pe.interbank.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Pedidos", description = "Operaciones relacionadas con pedidos")
@Validated
@RestController
@RequestMapping("/api")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Operation(summary = "Crear pedidos de productos", description = "Registra una lista de pedidos relacionados a los productos")
    @PostMapping("/pedidos")
    public Mono<ResponseEntity<PedidoResumenResponse>> procesarPedidos(@Valid @RequestBody List<PedidoRequest> pedidoRequests) {
        return pedidoService.procesarPedido(pedidoRequests)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
}
