package com.amc.api.Controllers.Order;

import com.amc.api.Entities.Order.Order;
import com.amc.api.Repositories.Order.OrderRepository;
import com.amc.api.Services.Order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService OrderService;

    @Autowired
    private OrderRepository OrderRepository;

    @GetMapping("/{uuid}")
    public ResponseEntity<Order> getOrderByUuid(@PathVariable("uuid") String orderUuid ) {
        Order order = OrderService.findOrderByUuid(orderUuid);
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> allOrders = OrderRepository.findAll();
        return allOrders.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(allOrders);
    }

//    @PostMapping
//    public ResponseEntity<Order> createOrder(@RequestBody Order newOrder, @RequestParam("userUuid") String userUuid) {
//        Order order = OrderService.createOrder(newOrder, userUuid);
//        return order != null ? ResponseEntity.ok(order) : ResponseEntity.badRequest().build();
//    }
//
//    @PutMapping("/{uuid}")
//    public ResponseEntity<Order> updateOrder(@PathVariable("uuid") String orderUuid, @RequestBody Order orderData) {
//        Order order = OrderService.updateOrder(orderData, orderUuid);
//        return order != null ? ResponseEntity.ok(order) : ResponseEntity.notFound().build();
//    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Order> deleteOrder(@PathVariable("uuid") String orderUuid ) {
        boolean order = OrderService.deleteOrder(orderUuid);
        return order ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

}
