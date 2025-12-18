package com.jspider.foodiesapi.service;

import com.jspider.foodiesapi.io.FoodRequest;
import com.jspider.foodiesapi.io.FoodResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FoodService {
String uploadFile(MultipartFile file);
FoodResponse addFood(FoodRequest foodReq, MultipartFile file);
List<FoodResponse> getFood();
FoodResponse readFood(String id);
boolean deleteFile(String filename);
void deleteFood(String id);
}
