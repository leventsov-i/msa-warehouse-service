package ru.bmtsu.warehouse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.bmtsu.warehouse.controller.dto.request.OrderItemWarrantyDTO;
import ru.bmtsu.warehouse.controller.dto.response.OrderItemWarrantyDecisionDTO;
import ru.bmtsu.warehouse.service.dto.WarrantyRequestDTO;
import ru.bmtsu.warehouse.service.dto.WarrantyResponseDTO;

import java.util.UUID;

@Service
public class WarrantyService {
    private final OrderItemService orderItemService;
    private final WarrantyExternService warrantyExternService;

    @Autowired
    public WarrantyService(OrderItemService orderItemService, WarrantyExternService warrantyExternService) {
        this.orderItemService = orderItemService;
        this.warrantyExternService = warrantyExternService;
    }

    public OrderItemWarrantyDecisionDTO warranty(UUID orderItem, OrderItemWarrantyDTO warranty) {

        WarrantyRequestDTO warrantyRequest = WarrantyRequestDTO.builder()
                .reason(warranty.getReason())
                .availableCount(orderItemService.getAvailableCountItems(orderItem))
                .build();

        WarrantyResponseDTO warrantyInformation = warrantyExternService.getWarrantyInformation(orderItem, warrantyRequest);

        return OrderItemWarrantyDecisionDTO.builder()
                .decision(warrantyInformation.getDecision())
                .warrantyDate(warrantyInformation.getWarrantyDate())
                .build();
    }
}
