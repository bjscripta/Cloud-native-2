package com.pedidos360.pedidosdomain.envios.config;

import com.pedidos360.messaging.RabbitTopology;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;

@Configuration
public class EnviosRabbitConfig {

    @Bean
    public DirectExchange enviosEventsExchange() {
        return new DirectExchange(RabbitTopology.EVENTS_EXCHANGE, true, false);
    }

    @Bean
    public DirectExchange enviosDeadLetterExchange() {
        return new DirectExchange(
                RabbitTopology.DEAD_LETTER_EXCHANGE, true, false);
    }

    @Bean
    public Queue enviosQueue() {
        return QueueBuilder.durable(RabbitTopology.ENVIOS_QUEUE)
                .deadLetterExchange(RabbitTopology.DEAD_LETTER_EXCHANGE)
                .deadLetterRoutingKey(RabbitTopology.ENVIOS_DEAD_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue enviosDlq() {
        return QueueBuilder.durable(RabbitTopology.ENVIOS_DLQ)
                .ttl(RabbitTopology.DLQ_MESSAGE_TTL_MS)
                .build();
    }

    @Bean
    public Binding enviosPedidoCreadoBinding(
            @Qualifier("enviosQueue") Queue enviosQueue,
            @Qualifier("enviosEventsExchange") DirectExchange enviosEventsExchange) {
        return BindingBuilder.bind(enviosQueue)
                .to(enviosEventsExchange)
                .with(RabbitTopology.PEDIDO_CREADO_ROUTING_KEY);
    }

    @Bean
    public Binding enviosDlqBinding(
            @Qualifier("enviosDlq") Queue enviosDlq,
            @Qualifier("enviosDeadLetterExchange") DirectExchange enviosDeadLetterExchange) {
        return BindingBuilder.bind(enviosDlq)
                .to(enviosDeadLetterExchange)
                .with(RabbitTopology.ENVIOS_DEAD_ROUTING_KEY);
    }
}
