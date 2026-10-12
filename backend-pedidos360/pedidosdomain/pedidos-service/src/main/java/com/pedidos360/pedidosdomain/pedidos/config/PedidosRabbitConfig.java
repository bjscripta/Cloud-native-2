package com.pedidos360.pedidosdomain.pedidos.config;

import com.pedidos360.messaging.RabbitTopology;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PedidosRabbitConfig {

    @Bean
    public DirectExchange pedidosEventsExchange() {
        return new DirectExchange(RabbitTopology.EVENTS_EXCHANGE, true, false);
    }
}
