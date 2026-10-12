package com.pedidos360.pedidosdomain.facturacion.messaging;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedidos360.messaging.PedidoCreadoEvent;
import com.pedidos360.messaging.RabbitTopology;
import com.pedidos360.pedidosdomain.facturacion.model.Facturacion;
import com.pedidos360.pedidosdomain.facturacion.repository.FacturacionRepository;
import com.rabbitmq.client.Channel;

@Component
public class PedidoCreadoConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PedidoCreadoConsumer.class);

    private final FacturacionRepository facturacionRepository;
    private final ObjectMapper objectMapper;

    public PedidoCreadoConsumer(
            FacturacionRepository facturacionRepository,
            ObjectMapper objectMapper) {
        this.facturacionRepository = facturacionRepository;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitTopology.FACTURACION_QUEUE)
    public void recibir(String contenido, Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            PedidoCreadoEvent evento = objectMapper.readValue(contenido, PedidoCreadoEvent.class);

            if (facturacionRepository.findByPedidoId(evento.pedidoId()).isEmpty()) {
                Facturacion factura = new Facturacion();
                factura.setPedidoId(evento.pedidoId());
                factura.setClienteId(evento.clienteId());
                factura.setMontoTotal(evento.total());
                factura.setEstadoPago("PENDIENTE");
                facturacionRepository.save(factura);
            }

            channel.basicAck(deliveryTag, false);
            LOGGER.info("Pedido {} facturado y confirmado con ACK", evento.pedidoId());
        } catch (Exception exception) {
            LOGGER.error("No fue posible procesar el pedido para facturacion", exception);
            throw new IllegalStateException("Error procesando mensaje de facturacion", exception);
        }
    }
}
