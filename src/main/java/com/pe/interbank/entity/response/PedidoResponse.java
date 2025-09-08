package com.pe.interbank.entity.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class PedidoResponse {
    private Long id;
    private Double total;
    private LocalDate date;
    private String state;
}
