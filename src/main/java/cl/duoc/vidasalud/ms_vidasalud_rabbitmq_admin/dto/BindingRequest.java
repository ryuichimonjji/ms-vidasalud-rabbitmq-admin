package cl.duoc.vidasalud.ms_vidasalud_rabbitmq_admin.dto;

import jakarta.validation.constraints.NotBlank;

public class BindingRequest {

    @NotBlank(message = "El nombre de la cola es obligatorio")
    private String queue;

    @NotBlank(message = "El nombre del exchange es obligatorio")
    private String exchange;

    private String routingKey = "";

    public String getQueue() { return queue; }
    public void setQueue(String queue) { this.queue = queue; }
    public String getExchange() { return exchange; }
    public void setExchange(String exchange) { this.exchange = exchange; }
    public String getRoutingKey() { return routingKey; }
    public void setRoutingKey(String routingKey) { this.routingKey = routingKey; }
}