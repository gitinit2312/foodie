package com.jspider.foodiesapi.service;

import com.jspider.foodiesapi.entity.CartEntity;
import com.jspider.foodiesapi.io.CartRequest;
import com.jspider.foodiesapi.io.CartResponse;
import com.jspider.foodiesapi.repository.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@AllArgsConstructor
@Service
public class CartServiceImpl implements CartService {
private final CartRepository cartRepository;
private final UserService userService;
    @Override
    public CartResponse addToCart(CartRequest cartRequest) {
       String userId = userService.UserById();
CartEntity cart =  cartRepository.findByUserId(userId).orElseGet(() -> new CartEntity(userId , new HashMap<String ,Integer>()));
Map<String,Integer> items = cart.getItems();
items.put(cartRequest.getFoodId(), items.getOrDefault(cartRequest.getFoodId() , 0) + 1);
cart.setItems(items);
cartRepository.save(cart);
return convertToResponse(cart);
    }

    private CartResponse convertToResponse(CartEntity cart) {
           return CartResponse.builder()
                   .userId(cart.getUserId())
                   .items(cart.getItems())
                   .build();
    }
}

