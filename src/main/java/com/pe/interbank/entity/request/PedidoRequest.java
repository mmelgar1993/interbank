package com.pe.interbank.entity.request;

import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
public class PedidoRequest {
    @NotNull(message = "El id no puede ser nulo")
    @Positive(message = "El id debe ser un número positivo")
    private Long id;

    @NotNull(message = "El stock no puede ser nulo")
    @Min(value = 1, message = "La amount no puede ser 0")
    private Integer amount;


}
