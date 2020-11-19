package ru.bmtsu.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bmtsu.warehouse.entity.OrderItem;

import java.util.Optional;
import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByOrderItemUid(UUID orderItem);
}
