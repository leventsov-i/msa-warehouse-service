package ru.bmtsu.warehouse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bmtsu.warehouse.controller.dto.OrderItemDTO;
import ru.bmtsu.warehouse.controller.dto.response.ItemInfoDTO;
import ru.bmtsu.warehouse.entity.Item;
import ru.bmtsu.warehouse.entity.OrderItem;
import ru.bmtsu.warehouse.exception.NotAvailableCountItemsException;
import ru.bmtsu.warehouse.exception.NotFoundItemException;
import ru.bmtsu.warehouse.exception.NotFoundOrderItemException;
import ru.bmtsu.warehouse.exception.OrderItemCanceledException;
import ru.bmtsu.warehouse.repository.ItemRepository;
import ru.bmtsu.warehouse.repository.OrderItemRepository;

import java.util.UUID;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository, ItemRepository itemRepository) {
        this.orderItemRepository = orderItemRepository;
        this.itemRepository = itemRepository;

        //todo drop after demonstration
        itemRepository.save(new Item(null, 10000, "Lego 8070", "M"));
        itemRepository.save(new Item(null, 10000, "Lego 42070", "L"));
        itemRepository.save(new Item(null, 10000, "Lego 8880", "L"));
    }

    public ItemInfoDTO getItemInfo(UUID orderItemUid) {
        OrderItem orderItem = orderItemRepository
                .findByOrderItemUid(orderItemUid)
                .orElseThrow(NotFoundOrderItemException::new);

        if (orderItem.getCanceled()) {
            throw new OrderItemCanceledException();
        }

        return ItemInfoDTO.from(orderItem);
    }

    @Transactional
    public OrderItemDTO createOrderItem(OrderItemDTO additionalOrderItemInfo) {
        Item item = itemRepository
                .findByModelAndSize(additionalOrderItemInfo.getModel(), additionalOrderItemInfo.getSize())
                .orElseThrow(NotFoundItemException::new);

        int availableCountItems = item.getAvailableCount();
        checkAvailableCountItems(availableCountItems);
        decreaseAvailableCountItems(availableCountItems, item);

        UUID orderItemUid = UUID.randomUUID();
        OrderItem orderItem = OrderItem.builder()
                .canceled(false)
                .item(item)
                .orderUid(additionalOrderItemInfo.getOrderUid())
                .orderItemUid(orderItemUid)
                .build();
        orderItemRepository.save(orderItem);

        additionalOrderItemInfo.setOrderItemUid(orderItemUid);
        return additionalOrderItemInfo;
    }

    @Transactional
    public void setCanceledOrderItem(UUID orderItemUid) {
        OrderItem orderItem = orderItemRepository
                .findByOrderItemUid(orderItemUid)
                .orElseThrow(NotFoundOrderItemException::new);

        orderItem.setCanceled(true);
        orderItemRepository.save(orderItem);
    }

    public int getAvailableCountItems(UUID orderItemUid) {
        OrderItem orderItem = orderItemRepository
                .findByOrderItemUid(orderItemUid)
                .orElseThrow(NotFoundOrderItemException::new);
        return orderItem.getItem().getAvailableCount();
    }

    private void checkAvailableCountItems(int availableCountItems) {
        if (availableCountItems <= 0) {
            throw new NotAvailableCountItemsException();
        }
    }

    private void decreaseAvailableCountItems(int availableCountItems, Item item) {
        item.setAvailableCount(availableCountItems - 1);
        itemRepository.save(item);
    }
}
