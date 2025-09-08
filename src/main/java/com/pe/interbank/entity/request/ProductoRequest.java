package com.pe.interbank.entity.request;

import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
public class ProductoRequest {
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Pattern(regexp = "^[\\p{L}]+(?:[\\s'\\-][\\p{L}]+)*$", message = "Nombre inválido")
    private String name;

    @NotNull(message = "El precio no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor que 0")
    private Double price;

    @NotNull(message = "El stock no puede ser nulo")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

}
