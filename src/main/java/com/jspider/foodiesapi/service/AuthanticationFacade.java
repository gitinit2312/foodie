package com.jspider.foodiesapi.service;

import org.springframework.security.core.Authentication;

public interface AuthanticationFacade {
    Authentication getAuthentication();
}
