package com.pe.interbank.entity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Producto {
    @Id
    private Long id;
    private String nombre;
    private Double precio;
    private Integer stock;

}
