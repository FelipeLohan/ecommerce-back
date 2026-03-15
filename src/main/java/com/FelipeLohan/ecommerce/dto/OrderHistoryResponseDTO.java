package com.FelipeLohan.ecommerce.dto;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import com.FelipeLohan.ecommerce.documents.OrderHistoryDocument;

public class OrderHistoryResponseDTO {

    private String id;
    private Long orderId;
    private Instant moment;
    private String status;
    private Instant paymentMoment;
    private String clientName;
    private String clientEmail;
    private List<ItemDTO> items;
    private Double total;

    public OrderHistoryResponseDTO(OrderHistoryDocument doc) {
        this.id = doc.getId();
        this.orderId = doc.getOrderId();
        this.moment = doc.getMoment();
        this.status = doc.getStatus();
        this.paymentMoment = doc.getPaymentMoment();
        if (doc.getClient() != null) {
            this.clientName = doc.getClient().getName();
            this.clientEmail = doc.getClient().getEmail();
        }
        this.items = doc.getItems() == null ? List.of() :
                doc.getItems().stream().map(ItemDTO::new).collect(Collectors.toList());
        this.total = doc.getTotal();
    }

    public String getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Instant getMoment() {
        return moment;
    }

    public String getStatus() {
        return status;
    }

    public Instant getPaymentMoment() {
        return paymentMoment;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public Double getTotal() {
        return total;
    }

    public static class ItemDTO {

        private Long productId;
        private String name;
        private Double price;
        private Integer quantity;
        private String imgUrl;
        private Double subTotal;

        public ItemDTO(OrderHistoryDocument.ItemInfo item) {
            this.productId = item.getProductId();
            this.name = item.getName();
            this.price = item.getPrice();
            this.quantity = item.getQuantity();
            this.imgUrl = item.getImgUrl();
            this.subTotal = item.getSubTotal();
        }

        public Long getProductId() {
            return productId;
        }

        public String getName() {
            return name;
        }

        public Double getPrice() {
            return price;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public Double getSubTotal() {
            return subTotal;
        }
    }
}
