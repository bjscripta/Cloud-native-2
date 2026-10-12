# Cluster RabbitMQ de Pedidos360

Esta carpeta contiene un cluster local de dos nodos RabbitMQ para el proyecto Pedidos360. Los nodos comparten la misma cookie de Erlang y se descubren mediante la red interna creada por Docker Compose.

## Puertos

| Nodo | AMQP | Administracion |
| --- | ---: | ---: |
| rabbitmq-1 | 5672 | 15672 |
| rabbitmq-2 | 5673 | 15673 |

Credenciales de desarrollo: `admin` / `admin123`.

## Ejecucion

```bash
docker compose up -d
docker compose ps
docker exec rabbitmq-1 rabbitmqctl cluster_status
```

Paneles de administracion:

- http://localhost:15672
- http://localhost:15673

Para detener el cluster sin eliminar los datos:

```bash
docker compose down
```

Para detenerlo y eliminar sus volumenes:

```bash
docker compose down -v
```
