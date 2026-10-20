# ms-vidasalud-rabbitmq-admin

Microservicio administrador de RabbitMQ del sistema VidaSalud. Expone una API REST para crear y eliminar colas, exchanges y bindings.

## Tecnologías
- Java 17, Spring Boot 4.1.1
- Spring AMQP (RabbitMQ), Bean Validation

## Estructura
- `controller/`: endpoints REST (`RabbitAdminController`)
- `service/`: toda la lógica con RabbitMQ (`RabbitAdminService`)
- `dto/`: requests con validación (`QueueRequest`, `ExchangeRequest`, `BindingRequest`)
- `config/`: bean `RabbitAdmin`
- `exception/`: manejo global de errores (400, 404, 500)
- `docker/`: `docker-compose.yml` con clúster RabbitMQ de dos nodos

## Endpoints (puerto 8083)

| Verbo | Ruta | Descripción |
|---|---|---|
| POST | `/api/queues` | Crea una cola (con DLQ opcional) |
| DELETE | `/api/queues/{name}` | Elimina una cola |
| POST | `/api/exchanges` | Crea un exchange (direct, topic, fanout, headers) |
| DELETE | `/api/exchanges/{name}` | Elimina un exchange |
| POST | `/api/bindings` | Crea un binding |
| DELETE | `/api/bindings` | Elimina un binding |

### Ejemplos

Cola con su DLQ:

    {"name": "vidasalud.tickets.queue", "deadLetterQueue": "vidasalud.tickets.dlq"}

Exchange:

    {"name": "vidasalud.tickets.exchange", "type": "direct"}

Binding:

    {"queue": "vidasalud.tickets.queue", "exchange": "vidasalud.tickets.exchange", "routingKey": "ticket.created"}

## Validaciones
- Los nombres no pueden estar vacíos y solo admiten letras, números, punto, guion y guion bajo (máx. 255).
- El tipo de exchange debe ser `direct`, `topic`, `fanout` o `headers`.
- Los errores devuelven 400 con el detalle de cada campo; eliminar algo inexistente devuelve 404.

## Clúster RabbitMQ (docker-compose)

    cd docker
    docker compose up -d
    docker exec rabbitmq1 rabbitmqctl cluster_status

- Nodo 1: AMQP 5672, panel 15672
- Nodo 2: AMQP 5673, panel 15673
- Panel de administración: http://localhost:15672 (guest / guest)

## Ejecución del microservicio

    ./mvnw spring-boot:run
