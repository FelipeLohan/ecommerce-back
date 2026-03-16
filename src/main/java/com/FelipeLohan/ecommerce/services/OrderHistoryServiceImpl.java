package com.FelipeLohan.ecommerce.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.FelipeLohan.ecommerce.documents.OrderHistoryDocument;
import com.FelipeLohan.ecommerce.dto.OrderDTO;
import com.FelipeLohan.ecommerce.dto.OrderHistoryResponseDTO;
import com.FelipeLohan.ecommerce.repositories.OrderHistoryRepository;
import com.FelipeLohan.ecommerce.services.interfaces.OrderHistoryService;

@Service
public class OrderHistoryServiceImpl implements OrderHistoryService {

    private final OrderHistoryRepository repository;

    public OrderHistoryServiceImpl(OrderHistoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveHistory(OrderDTO dto, String clientEmail) {
        OrderHistoryDocument doc = new OrderHistoryDocument();
        doc.setOrderId(dto.getId());
        doc.setMoment(dto.getMoment());
        doc.setStatus(dto.getStatus().name());
        doc.setPaymentMoment(dto.getPayment() != null ? dto.getPayment().getMoment() : null);
        doc.setTotal(dto.getTotal());

        if (dto.getClient() != null) {
            OrderHistoryDocument.ClientInfo clientInfo = new OrderHistoryDocument.ClientInfo(
                    dto.getClient().getId(),
                    dto.getClient().getName(),
                    clientEmail
            );
            doc.setClient(clientInfo);
        }

        List<OrderHistoryDocument.ItemInfo> items = dto.getItems().stream()
                .map(item -> new OrderHistoryDocument.ItemInfo(
                        item.getProductId(),
                        item.getName(),
                        item.getPrice(),
                        item.getQuantity(),
                        item.getImgUrl(),
                        item.getSubTotal()
                ))
                .collect(Collectors.toList());
        doc.setItems(items);

        repository.save(doc);
    }

    @Override
    public Page<OrderHistoryResponseDTO> findAll(Pageable pageable) {
        return repository.findAllByOrderByMomentDesc(pageable)
                .map(OrderHistoryResponseDTO::new);
    }

    @Override
    public Page<OrderHistoryResponseDTO> findByClientEmail(String email, Pageable pageable) {
        return repository.findByClientEmailOrderByMomentDesc(email, pageable)
                .map(OrderHistoryResponseDTO::new);
    }
}
