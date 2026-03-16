package com.FelipeLohan.ecommerce.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

import com.FelipeLohan.ecommerce.entities.OrderStatus;

@Schema(description = "Dados de um pedido")
public class OrderDTO {

    @Schema(description = "ID do pedido (gerado automaticamente)", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Data e hora de criação do pedido", accessMode = Schema.AccessMode.READ_ONLY)
    private Instant moment;

    @Schema(description = "Status do pedido. Valores: WAITING_PAYMENT, PAID, SHIPPED, DELIVERED, CANCELED", example = "WAITING_PAYMENT")
    private OrderStatus status;

    @Schema(description = "Dados resumidos do cliente (preenchido automaticamente na resposta)", accessMode = Schema.AccessMode.READ_ONLY)
    private ClientDTO client;

    @Schema(description = "Dados do pagamento (preenchido após confirmação)", accessMode = Schema.AccessMode.READ_ONLY)
    private PaymentDTO payment;

    @Schema(description = "Itens do pedido (obrigatório pelo menos um item)")
    @NotEmpty(message = "Deve ter pelo menos um item")
    private List<OrderItemDTO> items = new ArrayList<>();

    public OrderDTO() {
    }

    public OrderDTO(Long id, Instant moment, OrderStatus status, ClientDTO client, PaymentDTO payment) {
        this.id = id;
        this.moment = moment;
        this.status = status;
        this.client = client;
        this.payment = payment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getMoment() {
        return moment;
    }

    public void setMoment(Instant moment) {
        this.moment = moment;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public PaymentDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentDTO payment) {
        this.payment = payment;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }

    public Double getTotal() {
        double sum = 0.0;
        for (OrderItemDTO item : items) {
            sum += item.getSubTotal();
        }
        return sum;
    }
}
