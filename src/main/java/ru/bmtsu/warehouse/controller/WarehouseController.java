package ru.bmtsu.warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bmtsu.warehouse.controller.dto.ErrorDTO;
import ru.bmtsu.warehouse.controller.dto.OrderItemDTO;
import ru.bmtsu.warehouse.controller.dto.request.OrderItemWarrantyDTO;
import ru.bmtsu.warehouse.controller.dto.response.ItemInfoDTO;
import ru.bmtsu.warehouse.controller.dto.response.OrderItemWarrantyDecisionDTO;
import ru.bmtsu.warehouse.exception.*;
import ru.bmtsu.warehouse.service.OrderItemService;
import ru.bmtsu.warehouse.service.WarrantyService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/warehouse")
public class WarehouseController {
    private final OrderItemService orderItemService;
    private final WarrantyService warrantyService;

    @Autowired
    public WarehouseController(OrderItemService orderItemService, WarrantyService warrantyService) {
        this.orderItemService = orderItemService;
        this.warrantyService = warrantyService;
    }

    @GetMapping("/{orderItem}")
    public ResponseEntity<ItemInfoDTO> get(@PathVariable UUID orderItem) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderItemService.getItemInfo(orderItem));
    }

    @PostMapping()
    public ResponseEntity<OrderItemDTO> create(@RequestBody OrderItemDTO orderItemBody) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderItemService.createOrderItem(orderItemBody));
    }

    @SuppressWarnings("rawtypes")
    @DeleteMapping("/{orderItem}")
    public ResponseEntity delete(@PathVariable UUID orderItem) {
        orderItemService.setCanceledOrderItem(orderItem);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PostMapping("/{orderItem}/warranty")
    public ResponseEntity<OrderItemWarrantyDecisionDTO> warranty(@PathVariable UUID orderItem, @RequestBody OrderItemWarrantyDTO warranty) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(warrantyService.warranty(orderItem, warranty));
    }

    @ExceptionHandler({NotFoundWarrantyException.class, NotFoundItemException.class, NotFoundOrderItemException.class})
    public ResponseEntity<ErrorDTO> handlerNotFoundException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorDTO(e.getMessage()));
    }

    @ExceptionHandler(NotAvailableCountItemsException.class)
    public ResponseEntity<ErrorDTO> handlerItemNotAvailableException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorDTO(e.getMessage()));
    }

    @ExceptionHandler(ServiceNotAvailableException.class)
    public ResponseEntity<ErrorDTO> handlerServiceNotAvailable(Exception e) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorDTO(e.getMessage()));
    }
}
