package com.pedidos360.infrastructure.rabbitadmin.service;

import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.pedidos360.messaging.RabbitTopology;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Service;

@Service
public class RabbitManagementService {

    private static final List<String> QUEUES = List.of(
            RabbitTopology.FACTURACION_QUEUE,
            RabbitTopology.FACTURACION_DLQ,
            RabbitTopology.ENVIOS_QUEUE,
            RabbitTopology.ENVIOS_DLQ);

    private static final Set<String> PURGEABLE_DLQS = Set.of(
            RabbitTopology.FACTURACION_DLQ,
            RabbitTopology.ENVIOS_DLQ);

    private final RabbitAdmin rabbitAdmin;

    public RabbitManagementService(RabbitAdmin rabbitAdmin) {
        this.rabbitAdmin = rabbitAdmin;
    }

    public List<QueueStatus> getQueues() {
        return QUEUES.stream().map(this::getQueue).toList();
    }

    public QueueStatus getQueue(String name) {
        if (!QUEUES.contains(name)) {
            throw new IllegalArgumentException("Cola no administrada: " + name);
        }
        Properties properties = rabbitAdmin.getQueueProperties(name);
        if (properties == null) {
            return new QueueStatus(name, false, 0, 0);
        }
        return new QueueStatus(
                name,
                true,
                ((Number) properties.get(RabbitAdmin.QUEUE_MESSAGE_COUNT)).intValue(),
                ((Number) properties.get(RabbitAdmin.QUEUE_CONSUMER_COUNT)).intValue());
    }

    public int purgeDlq(String name) {
        if (!PURGEABLE_DLQS.contains(name)) {
            throw new IllegalArgumentException("Solo se permite vaciar una DLQ administrada");
        }
        return rabbitAdmin.purgeQueue(name);
    }

    public record QueueStatus(String name, boolean exists, int messages, int consumers) {
    }
}
