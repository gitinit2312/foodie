package com.jspider.foodiesapi.service;

import org.springframework.web.multipart.MultipartFile;

public interface FoodService {
String uploadFile(MultipartFile file);
}
