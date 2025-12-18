package com.jspider.foodiesapi.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private String id;
    private String userId;
    private Map<String , Integer> items;
}
