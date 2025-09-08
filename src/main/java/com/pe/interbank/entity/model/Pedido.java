package com.pe.interbank.entity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Pedido {
    @Id
    private Long id;
    private LocalDate fecha;
    private Double total;
    private String estado;

}
