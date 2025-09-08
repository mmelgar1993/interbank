package com.pe.interbank.repository;

import com.pe.interbank.entity.model.Pedido;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface PedidoRepository extends R2dbcRepository<Pedido,Long> {
}
