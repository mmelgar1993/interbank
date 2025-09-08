package com.pe.interbank.adapter;

import com.pe.interbank.entity.request.PedidoRequest;
import com.pe.interbank.entity.request.ProductoRequest;
import com.pe.interbank.entity.response.PedidoResponse;
import com.pe.interbank.entity.response.PedidoResumenResponse;
import com.pe.interbank.entity.response.ProductoResponse;
import com.pe.interbank.service.ProductoService;
import com.pe.interbank.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Component
public class PedidoAdapter {

    @Autowired
    ProductoService productoService;

    public Mono<PedidoResumenResponse> updateStockProductos(List<PedidoRequest> pedidoRequests) {
        return Flux.fromIterable(pedidoRequests)
                .flatMap(pr ->
                        calcularStock(pr)
                                .flatMap(nuevoStocks -> {
                                    if (nuevoStocks.getStock() < 0) {
                                        return Mono.error(new RuntimeException("Stock insuficiente para el producto con ID: " + pr.getId()));
                                    }

                                    ProductoRequest productoActualizado = new ProductoRequest(
                                            nuevoStocks.getName(),
                                            nuevoStocks.getPrice(),
                                            nuevoStocks.getStock()
                                    );

                                    return productoService.updateProducto(pr.getId().toString(), productoActualizado)
                                            .map(productoActualizadoResponse -> PedidoResponse.builder()
                                                    .id(pr.getId())
                                                    .state(Constantes.ACTIVO.getValor())
                                                    .date(LocalDate.now())
                                                    .total(pr.getAmount() * productoActualizadoResponse.getPrice())
                                                    .build());
                                })
                )
                .collectList()
                .map(lista -> {
                    double totalGlobal = lista.stream()
                            .mapToDouble(PedidoResponse::getTotal)
                            .sum();
                    double totalConDescuento = calcularTotalConDescuento(lista);

                    if (totalConDescuento < totalGlobal) {
                        totalGlobal = totalConDescuento;
                    }
                    return new PedidoResumenResponse(lista, totalGlobal);
                });

    }

    private Mono<ProductoResponse> calcularStock(PedidoRequest pr) {
        return productoService.findById(pr.getId().toString())
                .map(productoResponse -> {
                    int nuevoStock = productoResponse.getStock() - pr.getAmount();
                    return ProductoResponse.builder()
                            .id(productoResponse.getId())
                            .name(productoResponse.getName())
                            .price(productoResponse.getPrice())
                            .stock(nuevoStock)
                            .build();
                });
    }

    public Double calcularTotalConDescuento(List<PedidoResponse> pedidos) {
        double totalBruto = pedidos.stream()
                .mapToDouble(PedidoResponse::getTotal)
                .sum();

        int productosDistintos = (int) pedidos.stream()
                .map(PedidoResponse::getId) // o getProductoId si lo tienes
                .distinct()
                .count();

        double descuento = 0.0;

        if (totalBruto > 1000) {
            descuento += 0.10;
        }

        if (productosDistintos > 5) {
            descuento += 0.05;
        }

        return totalBruto * (1 - descuento);

    }
}
