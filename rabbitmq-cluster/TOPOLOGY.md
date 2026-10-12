# Topologia de mensajeria de Pedidos360

## Evento

`pedido.creado` se publica cuando `pedidos-service` guarda un pedido nuevo.

El mensaje contiene:

- `pedidoId`
- `clienteId`
- `total`
- `direccionEntrega`
- `fechaCreacion`

## Exchanges

| Nombre | Tipo | Proposito |
| --- | --- | --- |
| `pedidos360.events.exchange` | direct | Distribuir eventos del negocio mediante routing keys |
| `pedidos360.dlx.exchange` | direct | Recibir mensajes rechazados o agotados tras los reintentos |

## Colas principales

| Cola | Routing key | Consumidor |
| --- | --- | --- |
| `facturacion.pedido-creado.queue` | `pedido.creado` | `facturacion-service` |
| `envios.pedido-creado.queue` | `pedido.creado` | `envios-service` |

Las dos colas se enlazan con la misma routing key. De este modo, cada servicio recibe su propia copia del evento y lo procesa de manera independiente.

## Dead letter queues

| Cola principal | DLQ | Routing key de error |
| --- | --- | --- |
| `facturacion.pedido-creado.queue` | `facturacion.pedido-creado.dlq` | `facturacion.pedido-creado.dead` |
| `envios.pedido-creado.queue` | `envios.pedido-creado.dlq` | `envios.pedido-creado.dead` |

Los consumidores usan confirmacion manual. Cuando el procesamiento termina correctamente envian ACK. Tras agotar los reintentos, el mensaje se rechaza sin requeue y RabbitMQ lo dirige a la DLQ correspondiente.

## Cantidad definida

- 2 exchanges
- 2 colas principales
- 2 dead letter queues
- 4 bindings
- 1 productor inicial en `pedidos-service`
- 2 consumidores iniciales en `facturacion-service` y `envios-service`
