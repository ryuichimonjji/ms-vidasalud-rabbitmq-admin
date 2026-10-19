package cl.duoc.vidasalud.ms_vidasalud_rabbitmq_admin.service;

import cl.duoc.vidasalud.ms_vidasalud_rabbitmq_admin.dto.BindingRequest;
import cl.duoc.vidasalud.ms_vidasalud_rabbitmq_admin.dto.ExchangeRequest;
import cl.duoc.vidasalud.ms_vidasalud_rabbitmq_admin.dto.QueueRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RabbitAdminService {

    private static final Logger log = LoggerFactory.getLogger(RabbitAdminService.class);

    private final RabbitAdmin rabbitAdmin;

    public RabbitAdminService(RabbitAdmin rabbitAdmin) {
        this.rabbitAdmin = rabbitAdmin;
    }

    // ---------- Colas ----------

    public Map<String, Object> createQueue(QueueRequest request) {
        String dlq = request.getDeadLetterQueue();
        boolean conDlq = dlq != null && !dlq.isBlank();

        if (conDlq) {
            // La DLQ se crea primero, enlazada a un exchange dlx propio de la cola.
            String dlx = request.getName() + ".dlx";
            rabbitAdmin.declareExchange(ExchangeBuilder.directExchange(dlx).durable(true).build());
            Queue deadQueue = QueueBuilder.durable(dlq).build();
            rabbitAdmin.declareQueue(deadQueue);
            rabbitAdmin.declareBinding(new Binding(dlq, Binding.DestinationType.QUEUE, dlx, dlq, null));

            Queue queue = QueueBuilder.durable(request.getName())
                    .withArgument("x-dead-letter-exchange", dlx)
                    .withArgument("x-dead-letter-routing-key", dlq)
                    .build();
            rabbitAdmin.declareQueue(queue);
            log.info("Cola creada: {} con DLQ {} (dlx={})", request.getName(), dlq, dlx);
        } else {
            Queue queue = request.isDurable()
                    ? QueueBuilder.durable(request.getName()).build()
                    : QueueBuilder.nonDurable(request.getName()).build();
            rabbitAdmin.declareQueue(queue);
            log.info("Cola creada: {}", request.getName());
        }
        return Map.of("name", request.getName(), "deadLetterQueue", conDlq ? dlq : "");
    }

    public void deleteQueue(String name) {
        boolean eliminada = rabbitAdmin.deleteQueue(name);
        if (!eliminada) {
            throw new IllegalArgumentException("La cola no existe: " + name);
        }
        log.info("Cola eliminada: {}", name);
    }

    // ---------- Exchanges ----------

    public Map<String, Object> createExchange(ExchangeRequest request) {
        Exchange exchange = ExchangeBuilder.directExchange(request.getName()).build();
        switch (request.getType()) {
            case "direct" -> exchange = ExchangeBuilder.directExchange(request.getName())
                    .durable(request.isDurable()).build();
            case "topic" -> exchange = ExchangeBuilder.topicExchange(request.getName())
                    .durable(request.isDurable()).build();
            case "fanout" -> exchange = ExchangeBuilder.fanoutExchange(request.getName())
                    .durable(request.isDurable()).build();
            case "headers" -> exchange = ExchangeBuilder.headersExchange(request.getName())
                    .durable(request.isDurable()).build();
            default -> throw new IllegalArgumentException("Tipo de exchange invalido: " + request.getType());
        }
        rabbitAdmin.declareExchange(exchange);
        log.info("Exchange creado: {} ({})", request.getName(), request.getType());
        return Map.of("name", request.getName(), "type", request.getType());
    }

    public void deleteExchange(String name) {
        boolean eliminado = rabbitAdmin.deleteExchange(name);
        if (!eliminado) {
            throw new IllegalArgumentException("El exchange no existe: " + name);
        }
        log.info("Exchange eliminado: {}", name);
    }

    // ---------- Bindings ----------

    public Map<String, Object> createBinding(BindingRequest request) {
        Binding binding = new Binding(request.getQueue(), Binding.DestinationType.QUEUE,
                request.getExchange(), request.getRoutingKey(), null);
        rabbitAdmin.declareBinding(binding);
        log.info("Binding creado: {} -> {} (routingKey='{}')",
                request.getExchange(), request.getQueue(), request.getRoutingKey());
        return Map.of("queue", request.getQueue(), "exchange", request.getExchange(),
                "routingKey", request.getRoutingKey());
    }

    public void deleteBinding(BindingRequest request) {
        Binding binding = new Binding(request.getQueue(), Binding.DestinationType.QUEUE,
                request.getExchange(), request.getRoutingKey(), null);
        rabbitAdmin.removeBinding(binding);
        log.info("Binding eliminado: {} -> {} (routingKey='{}')",
                request.getExchange(), request.getQueue(), request.getRoutingKey());
    }
}