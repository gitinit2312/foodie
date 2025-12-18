package com.jspider.foodiesapi.service;

import com.jspider.foodiesapi.io.UserRequest;
import com.jspider.foodiesapi.io.UserResponse;

public interface UserService {
    UserResponse registerUser(UserRequest user);
    String UserById();
}
