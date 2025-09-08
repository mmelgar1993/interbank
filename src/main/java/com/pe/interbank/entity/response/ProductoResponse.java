package com.pe.interbank.entity.response;

import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class ProductoResponse {
    private Long id;
    private String name;
    private Double price;
    private Integer stock;
}
