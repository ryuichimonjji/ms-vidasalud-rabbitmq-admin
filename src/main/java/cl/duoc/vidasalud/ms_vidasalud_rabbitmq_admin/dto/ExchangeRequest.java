package cl.duoc.vidasalud.ms_vidasalud_rabbitmq_admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ExchangeRequest {

    @NotBlank(message = "El nombre del exchange es obligatorio")
    @Size(max = 255, message = "El nombre no puede superar 255 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9._-]+$",
             message = "El nombre solo admite letras, numeros, punto, guion y guion bajo")
    private String name;

    @NotBlank(message = "El tipo es obligatorio")
    @Pattern(regexp = "^(direct|topic|fanout|headers)$",
             message = "El tipo debe ser direct, topic, fanout o headers")
    private String type;

    private boolean durable = true;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public boolean isDurable() { return durable; }
    public void setDurable(boolean durable) { this.durable = durable; }
}