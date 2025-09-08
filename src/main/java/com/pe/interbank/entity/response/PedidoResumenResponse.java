package com.pe.interbank.entity.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class PedidoResumenResponse {
    private List<PedidoResponse> pedidos;
    private double totalGlobal;

}
