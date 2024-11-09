package com.cocktailnight.cocktail_backend.service;

import com.cocktailnight.cocktail_backend.model.Order;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class OrderService {
    private final List<Order> orders = new CopyOnWriteArrayList<>();

    public void addOrder(Order order) {
        orders.add(order);
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void markOrderAsCompleted(int index) {
        if (index >= 0 && index < orders.size()) {
            orders.get(index).setCompleted(true);
        }
    }

    public Order getOrderById(String id) {
        return orders.stream()
                .filter(order -> order.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
