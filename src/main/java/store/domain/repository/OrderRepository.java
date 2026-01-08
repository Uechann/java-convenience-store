package store.domain.repository;

import store.domain.model.Order;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class OrderRepository {

    private final Map<Long, Order> orderMap = new HashMap<>();
    private final AtomicLong sequence = new AtomicLong(0L);

    public List<Order> findPendingOrders() {
        List<Order> orders = new ArrayList<>(orderMap.values());
        return orders.stream()
                .filter(Order::isPending)
                .toList();
    }

    public void save(Order order) {
        setIdByReflection(order, sequence.incrementAndGet());
        orderMap.put(order.getId(), order);
    }

    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(orderMap.get(id));
    }

    private void setIdByReflection(Order order, Long id) {
        try {
            Field idField = Order.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(order, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
}
