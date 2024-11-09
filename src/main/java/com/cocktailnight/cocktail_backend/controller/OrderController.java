package com.cocktailnight.cocktail_backend.controller;

import com.cocktailnight.cocktail_backend.model.Order;
import com.cocktailnight.cocktail_backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Store emitters for the bartender
    private List<SseEmitter> bartenderEmitters = new CopyOnWriteArrayList<>();

    // Store emitters for customers, keyed by order ID
    private Map<String, SseEmitter> customerEmitters = new HashMap<>();

    @PostMapping("/order")
    public Order placeOrder(@RequestBody Order order) {
        orderService.addOrder(order);
        // Notify bartender of new order
        sendUpdateToBartenders(order);
        return order; // Return the order with ID
    }

    @GetMapping("/bartender/orders")
    public List<Order> getOrders() {
        return orderService.getOrders();
    }

    @PostMapping("/bartender/orders/{id}/complete")
    public void completeOrder(@PathVariable String id) {
        Order order = orderService.getOrderById(id);
        if (order != null && !order.isCompleted()) {
            order.setCompleted(true);
            // Notify bartender of order completion
            sendUpdateToBartenders(order);
            // Notify customer that their order is completed
            sendUpdateToCustomer(order);
        }
    }

    // SSE endpoint for bartender
    @GetMapping("/bartender/stream")
    public SseEmitter streamToBartender() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        bartenderEmitters.add(emitter);
        emitter.onCompletion(() -> bartenderEmitters.remove(emitter));
        emitter.onTimeout(() -> bartenderEmitters.remove(emitter));
        return emitter;
    }

    // SSE endpoint for customer
    @GetMapping("/customer/stream/{orderId}")
    public SseEmitter streamToCustomer(@PathVariable String orderId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        customerEmitters.put(orderId, emitter);
        emitter.onCompletion(() -> customerEmitters.remove(orderId));
        emitter.onTimeout(() -> customerEmitters.remove(orderId));
        return emitter;
    }

    private void sendUpdateToBartenders(Order order) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        bartenderEmitters.forEach(emitter -> {
            try {
                emitter.send(order, MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        });
        bartenderEmitters.removeAll(deadEmitters);
    }

    private void sendUpdateToCustomer(Order order) {
        SseEmitter emitter = customerEmitters.get(order.getId());
        if (emitter != null) {
            try {
                emitter.send("Your " + order.getDrinkName() + " is ready!");
                emitter.complete();
            } catch (IOException e) {
                customerEmitters.remove(order.getId());
            }
        }
    }
}