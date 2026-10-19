package cl.duoc.vidasalud.ms_vidasalud_rabbitmq_admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class QueueRequest {

    @NotBlank(message = "El nombre de la cola es obligatorio")
    @Size(max = 255, message = "El nombre no puede superar 255 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9._-]+$",
             message = "El nombre solo admite letras, numeros, punto, guion y guion bajo")
    private String name;

    private boolean durable = true;

    @Size(max = 255, message = "El nombre de la DLQ no puede superar 255 caracteres")
    @Pattern(regexp = "^$|^[A-Za-z0-9._-]+$",
             message = "El nombre de la DLQ solo admite letras, numeros, punto, guion y guion bajo")
    private String deadLetterQueue;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public boolean isDurable() { return durable; }
    public void setDurable(boolean durable) { this.durable = durable; }
    public String getDeadLetterQueue() { return deadLetterQueue; }
    public void setDeadLetterQueue(String deadLetterQueue) { this.deadLetterQueue = deadLetterQueue; }
}