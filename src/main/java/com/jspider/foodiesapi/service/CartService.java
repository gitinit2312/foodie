package com.jspider.foodiesapi.service;

import com.jspider.foodiesapi.io.CartRequest;
import com.jspider.foodiesapi.io.CartResponse;

public interface CartService  {
    CartResponse addToCart(CartRequest cartRequest);
}
