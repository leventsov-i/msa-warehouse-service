package ru.bmtsu.warehouse.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class OrderItemWarrantyDecisionDTO {
    private String warrantyDate;
    private String decision;
}
