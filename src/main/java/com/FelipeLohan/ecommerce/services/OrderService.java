package com.FelipeLohan.ecommerce.services;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.FelipeLohan.ecommerce.dto.OrderDTO;
import com.FelipeLohan.ecommerce.dto.OrderItemDTO;
import com.FelipeLohan.ecommerce.entities.Order;
import com.FelipeLohan.ecommerce.entities.OrderItem;
import com.FelipeLohan.ecommerce.entities.OrderStatus;
import com.FelipeLohan.ecommerce.entities.Product;
import com.FelipeLohan.ecommerce.entities.User;
import com.FelipeLohan.ecommerce.repositories.OrderItemRepository;
import com.FelipeLohan.ecommerce.repositories.OrderRepository;
import com.FelipeLohan.ecommerce.repositories.ProductRepository;
import com.FelipeLohan.ecommerce.services.exceptions.ResourceNotFoundException;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository repository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private OrderHistoryService orderHistoryService;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Order order = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado"));
        authService.validateSelfOrAdmin(order.getClient().getId());
        return new OrderDTO(order);
    }

    @Transactional
    public OrderDTO insert(OrderDTO dto) {

        Order order = new Order();

        order.setMoment(Instant.now());
        order.setStatus(OrderStatus.WAITING_PAYMENT);

        User user = userService.authenticated();
        order.setClient(user);

        for (OrderItemDTO itemDto : dto.getItems()) {
            Product product = productRepository.getReferenceById(itemDto.getProductId());
            OrderItem item = new OrderItem(order, product, itemDto.getQuantity(), product.getPrice());
            order.getItems().add(item);
        }

        repository.save(order);
        orderItemRepository.saveAll(order.getItems());

        OrderDTO result = new OrderDTO(order);

        try {
            orderHistoryService.saveHistory(result, user.getEmail());
        } catch (Exception e) {
            logger.error("Falha ao salvar histórico do pedido {} no MongoDB: {}", order.getId(), e.getMessage());
        }

        return result;
    }
}
