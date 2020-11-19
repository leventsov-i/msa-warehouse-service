package ru.bmtsu.warehouse.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class WarrantyRequestDTO {
    private String reason;
    private Integer availableCount;
}
