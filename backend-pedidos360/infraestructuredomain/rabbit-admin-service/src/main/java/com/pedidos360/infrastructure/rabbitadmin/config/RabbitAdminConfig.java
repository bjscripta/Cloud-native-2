package com.pedidos360.infrastructure.rabbitadmin.config;

import com.pedidos360.messaging.RabbitTopology;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitAdminConfig {

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    @Bean
    public Declarables pedidos360Topology() {
        DirectExchange events = new DirectExchange(RabbitTopology.EVENTS_EXCHANGE, true, false);
        DirectExchange dlx = new DirectExchange(RabbitTopology.DEAD_LETTER_EXCHANGE, true, false);

        Queue facturacion = QueueBuilder.durable(RabbitTopology.FACTURACION_QUEUE)
                .deadLetterExchange(RabbitTopology.DEAD_LETTER_EXCHANGE)
                .deadLetterRoutingKey(RabbitTopology.FACTURACION_DEAD_ROUTING_KEY)
                .build();
        Queue facturacionDlq = QueueBuilder.durable(RabbitTopology.FACTURACION_DLQ)
                .ttl(RabbitTopology.DLQ_MESSAGE_TTL_MS).build();
        Queue envios = QueueBuilder.durable(RabbitTopology.ENVIOS_QUEUE)
                .deadLetterExchange(RabbitTopology.DEAD_LETTER_EXCHANGE)
                .deadLetterRoutingKey(RabbitTopology.ENVIOS_DEAD_ROUTING_KEY)
                .build();
        Queue enviosDlq = QueueBuilder.durable(RabbitTopology.ENVIOS_DLQ)
                .ttl(RabbitTopology.DLQ_MESSAGE_TTL_MS).build();

        return new Declarables(
                events, dlx, facturacion, facturacionDlq, envios, enviosDlq,
                BindingBuilder.bind(facturacion).to(events)
                        .with(RabbitTopology.PEDIDO_CREADO_ROUTING_KEY),
                BindingBuilder.bind(envios).to(events)
                        .with(RabbitTopology.PEDIDO_CREADO_ROUTING_KEY),
                BindingBuilder.bind(facturacionDlq).to(dlx)
                        .with(RabbitTopology.FACTURACION_DEAD_ROUTING_KEY),
                BindingBuilder.bind(enviosDlq).to(dlx)
                        .with(RabbitTopology.ENVIOS_DEAD_ROUTING_KEY));
    }
}
