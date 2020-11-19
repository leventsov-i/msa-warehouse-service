package ru.bmtsu.warehouse.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class WarrantyResponseDTO {
    private String warrantyDate;
    private String decision;
}
