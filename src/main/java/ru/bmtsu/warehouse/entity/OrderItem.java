package ru.bmtsu.warehouse.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "canceled", nullable = false)
    private Boolean canceled;

    @Column(name = "order_item_uid", nullable = false, unique = true)
    private UUID orderItemUid;

    @Column(name = "order_uid", nullable = false)
    private UUID orderUid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
}
