package ru.bmtsu.warehouse.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.bmtsu.warehouse.entity.Item;
import ru.bmtsu.warehouse.entity.OrderItem;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ItemInfoDTO {
    private String model;
    private String size;

    public static ItemInfoDTO from(OrderItem orderItem) {
        Item item = orderItem.getItem();
        return ItemInfoDTO.builder()
                .model(item.getModel())
                .size(item.getSize())
                .build();
    }
}
