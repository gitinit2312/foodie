package com.jspider.foodiesapi.controller;
import com.jspider.foodiesapi.io.FoodRequest;
import com.jspider.foodiesapi.io.FoodResponse;
import com.jspider.foodiesapi.service.FoodService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
@AllArgsConstructor
public class FoodController {
    private final FoodService foodService;

    @PostMapping
    public FoodResponse addFood(@RequestPart("food") /*String FoodString*/ FoodRequest food, @RequestPart("file") MultipartFile file) {
//        ObjectMapper objMapper = new ObjectMapper();
//        FoodRequest request = null;
//        try {
//            request = objMapper.readValue(foodString, FoodRequest.class);
//        } catch (JsonProcessingException e) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Json Format");
//        }
        return foodService.addFood(food, file);
    }


    @GetMapping
    public List<FoodResponse> readFoods() {
        return foodService.getFood();
    }

    @GetMapping("/{id}")
    public FoodResponse readFood(@PathVariable String id) {
        return foodService.readFood(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFood(@PathVariable String id) {
         foodService.deleteFood(id);
    }
}