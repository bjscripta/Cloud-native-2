package com.pedidos360.messaging;

import java.time.LocalDateTime;

public record PedidoCreadoEvent(
        long pedidoId,
        long clienteId,
        float total,
        String direccionEntrega,
        LocalDateTime fechaCreacion) {
}
