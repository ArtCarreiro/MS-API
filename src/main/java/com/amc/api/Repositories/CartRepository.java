package com.amc.api.Repositories;

import com.amc.api.Entities.Cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {

    Cart findByUuid(String uuid);

}
