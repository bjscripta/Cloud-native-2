package com.pedidos360.pedidosdomain.facturacion.config;

import com.pedidos360.messaging.RabbitTopology;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FacturacionRabbitConfig {

    @Bean
    public DirectExchange facturacionEventsExchange() {
        return new DirectExchange(RabbitTopology.EVENTS_EXCHANGE, true, false);
    }

    @Bean
    public DirectExchange facturacionDeadLetterExchange() {
        return new DirectExchange(
                RabbitTopology.DEAD_LETTER_EXCHANGE, true, false);
    }

    @Bean
    public Queue facturacionQueue() {
        return QueueBuilder.durable(RabbitTopology.FACTURACION_QUEUE)
                .deadLetterExchange(RabbitTopology.DEAD_LETTER_EXCHANGE)
                .deadLetterRoutingKey(
                        RabbitTopology.FACTURACION_DEAD_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue facturacionDlq() {
        return QueueBuilder.durable(RabbitTopology.FACTURACION_DLQ)
                .ttl(RabbitTopology.DLQ_MESSAGE_TTL_MS)
                .build();
    }

    @Bean
    public Binding facturacionPedidoCreadoBinding(
            Queue facturacionQueue,
            DirectExchange facturacionEventsExchange) {
        return BindingBuilder.bind(facturacionQueue)
                .to(facturacionEventsExchange)
                .with(RabbitTopology.PEDIDO_CREADO_ROUTING_KEY);
    }

    @Bean
    public Binding facturacionDlqBinding(
            Queue facturacionDlq,
            DirectExchange facturacionDeadLetterExchange) {
        return BindingBuilder.bind(facturacionDlq)
                .to(facturacionDeadLetterExchange)
                .with(RabbitTopology.FACTURACION_DEAD_ROUTING_KEY);
    }
}
