package com.FelipeLohan.ecommerce.documents;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "order_history")
public class OrderHistoryDocument {

    @Id
    private String id;

    private Long orderId;
    private Instant moment;
    private String status;
    private Instant paymentMoment;
    private ClientInfo client;
    private List<ItemInfo> items;
    private Double total;

    public OrderHistoryDocument() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Instant getMoment() {
        return moment;
    }

    public void setMoment(Instant moment) {
        this.moment = moment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getPaymentMoment() {
        return paymentMoment;
    }

    public void setPaymentMoment(Instant paymentMoment) {
        this.paymentMoment = paymentMoment;
    }

    public ClientInfo getClient() {
        return client;
    }

    public void setClient(ClientInfo client) {
        this.client = client;
    }

    public List<ItemInfo> getItems() {
        return items;
    }

    public void setItems(List<ItemInfo> items) {
        this.items = items;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public static class ClientInfo {

        private Long id;
        private String name;
        private String email;

        public ClientInfo() {
        }

        public ClientInfo(Long id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class ItemInfo {

        private Long productId;
        private String name;
        private Double price;
        private Integer quantity;
        private String imgUrl;
        private Double subTotal;

        public ItemInfo() {
        }

        public ItemInfo(Long productId, String name, Double price, Integer quantity, String imgUrl, Double subTotal) {
            this.productId = productId;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.imgUrl = imgUrl;
            this.subTotal = subTotal;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public Double getSubTotal() {
            return subTotal;
        }

        public void setSubTotal(Double subTotal) {
            this.subTotal = subTotal;
        }
    }
}
