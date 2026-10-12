package com.pedidos360.pedidosdomain.envios.messaging;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedidos360.messaging.PedidoCreadoEvent;
import com.pedidos360.messaging.RabbitTopology;
import com.pedidos360.pedidosdomain.envios.model.Envios;
import com.pedidos360.pedidosdomain.envios.repository.EnviosRepository;
import com.rabbitmq.client.Channel;

@Component
public class PedidoCreadoConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PedidoCreadoConsumer.class);

    private final EnviosRepository enviosRepository;
    private final ObjectMapper objectMapper;

    public PedidoCreadoConsumer(EnviosRepository enviosRepository, ObjectMapper objectMapper) {
        this.enviosRepository = enviosRepository;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitTopology.ENVIOS_QUEUE)
    public void recibir(String contenido, Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            PedidoCreadoEvent evento = objectMapper.readValue(contenido, PedidoCreadoEvent.class);

            if (enviosRepository.findByPedidoId(evento.pedidoId()).isEmpty()) {
                Envios envio = new Envios();
                envio.setPedidoId(evento.pedidoId());
                envio.setDireccionEntrega(evento.direccionEntrega());
                envio.setEstadoEnvio("EN_PREPARACION");
                envio.setNumeroSeguimiento(UUID.randomUUID().toString());
                enviosRepository.save(envio);
            }

            channel.basicAck(deliveryTag, false);
            LOGGER.info("Pedido {} preparado para envio y confirmado con ACK", evento.pedidoId());
        } catch (Exception exception) {
            LOGGER.error("No fue posible procesar el pedido para envio", exception);
            throw new IllegalStateException("Error procesando mensaje de envio", exception);
        }
    }
}
