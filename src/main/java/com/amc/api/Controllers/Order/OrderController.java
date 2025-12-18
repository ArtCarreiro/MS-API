package com.amc.api.Controllers.Order;

import com.amc.api.Entities.Order.Order;
import com.amc.api.Repositories.Order.OrderRepository;
import com.amc.api.Services.Order.OrderService;
import com.amc.api.Utils.Exceptions;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/{uuid}")
    public ResponseEntity<Order> getOrderByUuid(@PathVariable("uuid") String orderUuid ) {
        Order order = orderService.findOrderByUuid(orderUuid);
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> allOrders = orderRepository.findAll();
        return allOrders.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(allOrders);
    }

//    @PostMapping
//    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order newOrder, @RequestParam("userUuid") String userUuid) {
//        Order order = orderService.createOrder(newOrder, userUuid);
//        return order != null ? ResponseEntity.ok(order) : ResponseEntity.badRequest().build();
//    }
//
//    @PutMapping("/{uuid}")
//    public ResponseEntity<Order> updateOrder(@Valid @PathVariable("uuid") String orderUuid, @RequestBody Order orderData) {
//        Order order = orderService.updateOrder(orderData, orderUuid);
//        return order != null ? ResponseEntity.ok(order) : ResponseEntity.notFound().build();
//    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Order> deleteOrder(@PathVariable("uuid") String orderUuid ) {
        if (orderRepository.findByUuid(orderUuid) == null)
            throw new Exceptions.ResourceNotFoundException("Pedido não encontrado.");
        boolean order = orderService.deleteOrder(orderUuid);
        return order ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();
    }

}
