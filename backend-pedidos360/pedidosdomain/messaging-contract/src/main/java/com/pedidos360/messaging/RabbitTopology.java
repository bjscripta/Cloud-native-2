package com.pedidos360.messaging;

public final class RabbitTopology {

    public static final String EVENTS_EXCHANGE = "pedidos360.events.exchange";
    public static final String DEAD_LETTER_EXCHANGE = "pedidos360.dlx.exchange";

    public static final String PEDIDO_CREADO_ROUTING_KEY = "pedido.creado";

    public static final String FACTURACION_QUEUE = "facturacion.pedido-creado.queue";
    public static final String FACTURACION_DLQ = "facturacion.pedido-creado.dlq";
    public static final String FACTURACION_DEAD_ROUTING_KEY =
            "facturacion.pedido-creado.dead";

    public static final String ENVIOS_QUEUE = "envios.pedido-creado.queue";
    public static final String ENVIOS_DLQ = "envios.pedido-creado.dlq";
    public static final String ENVIOS_DEAD_ROUTING_KEY =
            "envios.pedido-creado.dead";

    public static final int DLQ_MESSAGE_TTL_MS = 86_400_000;

    private RabbitTopology() {
    }
}
