package com.FelipeLohan.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Item de um pedido")
public class OrderItemDTO {

    @Schema(description = "ID do produto", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long productId;

    @Schema(description = "Nome do produto (preenchido automaticamente na resposta)", accessMode = Schema.AccessMode.READ_ONLY)
    private String name;

    @Schema(description = "Preço unitário do produto (preenchido automaticamente na resposta)", accessMode = Schema.AccessMode.READ_ONLY)
    private Double price;

    @Schema(description = "Quantidade do produto no pedido", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer quantity;

    @Schema(description = "URL da imagem do produto (preenchido automaticamente na resposta)", accessMode = Schema.AccessMode.READ_ONLY)
    private String imgUrl;

    public OrderItemDTO() {
    }

    public OrderItemDTO(Long productId, String name, Double price, Integer quantity, String imgUrl) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imgUrl = imgUrl;
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

    public Double getSubTotal() {
        return price * quantity;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
