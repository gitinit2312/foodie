package com.jspider.foodiesapi.controller;
import com.jspider.foodiesapi.io.CartRequest;
import com.jspider.foodiesapi.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@AllArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    @PostMapping
    public ResponseEntity<?> addToCart (@RequestBody CartRequest request) {
        String foodId= request.getFoodId();

        if (foodId== null || foodId.isEmpty()){
        return ResponseEntity.badRequest().body("Food Id is required");
}
        cartService.addToCart(request);
        return ResponseEntity.ok().body(null);
    }
}
