package com.pe.interbank.repository;

import com.pe.interbank.entity.model.Producto;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface ProductoRepository extends R2dbcRepository<Producto,Long> {
}
