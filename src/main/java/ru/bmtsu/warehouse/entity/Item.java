package ru.bmtsu.warehouse.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "available_count", nullable = false)
    private Integer availableCount;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "size", nullable = false)
    private String size;
}
