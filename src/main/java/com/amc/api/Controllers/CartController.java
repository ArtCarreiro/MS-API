package com.amc.api.Controllers;


import com.amc.api.Entities.Cart.Cart;
import com.amc.api.Repositories.CartRepository;
import com.amc.api.Services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private CartService cartService;
    private CartRepository cartRepository;

//    @GetMapping("/{uuid}")
//    public ResponseEntity<Cart> getCart(@PathVariable(name = "uuid") String uuid) {
//        try {
//
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//
//    }
//
//    @DeleteMapping("/{uuid}")
//    public ResponseEntity<Cart> cleanCart(@PathVariable(name = "uuid") String uuid) {
//
//
//
//
//    }




}
/*
- `GET /cart` - Obter carrinho do usuário
- `POST /cart/items` - Adicionar item ao carrinho
  - Body: `{ productId: string, quantity: number }`
        - `PUT /cart/items/{productId}` - Atualizar quantidade
  - Body: `{ quantity: number }`
        - `DELETE /cart/items/{productId}` - Remover item
- `DELETE /cart` - Limpar carrinho
*/