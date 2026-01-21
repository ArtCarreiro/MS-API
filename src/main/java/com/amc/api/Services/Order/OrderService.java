package com.amc.api.Services.Order;

import com.amc.api.Entities.Order.Order;
import com.amc.api.Repositories.Order.OrderRepository;
import com.amc.api.Repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order findOrderByUuid(String OrderUuid) {
        try {
            return orderRepository.findByUuid(OrderUuid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Order findAllOrderByUserUuid(String userUuid) {
        try {
            return orderRepository.findByUuid(userUuid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    @Transactional
//    public Order createOrder(Order newOrder, String userUuid) {
//        try {
//            User user = userRepository.findByUuid(userUuid);
//            if (user != null) {
//                userRepository.save(user);
//            }
//            return orderRepository.save(newOrder);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Transactional
//    public Order updateOrder(Order updatedOrder, String orderUuid) {
//        modelMapper.typeMap(Order.class, Order.class)
//                .addMappings(mapper -> mapper.skip(Order::setUuid));
//        try {
//            Order existingOrder = orderRepository.findByUuid(orderUuid);
//            if (existingOrder != null) {
//                modelMapper.map(updatedOrder, existingOrder);
//                return orderRepository.save(existingOrder);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        return null;
//    }

    @Transactional
    public boolean deleteOrder(String OrderUuid) {
        try {
            Order Order = orderRepository.findByUuid(OrderUuid);
            if (Order != null) {
                orderRepository.delete(Order);
                return true;
            } else
                return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
