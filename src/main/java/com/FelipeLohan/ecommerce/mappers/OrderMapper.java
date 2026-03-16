package com.FelipeLohan.ecommerce.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.FelipeLohan.ecommerce.dto.ClientDTO;
import com.FelipeLohan.ecommerce.dto.OrderDTO;
import com.FelipeLohan.ecommerce.dto.OrderItemDTO;
import com.FelipeLohan.ecommerce.dto.PaymentDTO;
import com.FelipeLohan.ecommerce.entities.Order;
import com.FelipeLohan.ecommerce.entities.OrderItem;
import com.FelipeLohan.ecommerce.entities.Payment;
import com.FelipeLohan.ecommerce.entities.User;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDTO toDTO(Order order);

    ClientDTO clientToDTO(User user);

    PaymentDTO paymentToDTO(Payment payment);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "imgUrl", source = "product.imgUrl")
    OrderItemDTO orderItemToDTO(OrderItem item);
}
