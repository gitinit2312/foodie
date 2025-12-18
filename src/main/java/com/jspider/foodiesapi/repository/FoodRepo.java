package com.jspider.foodiesapi.repository;

import com.jspider.foodiesapi.entity.FoodEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepo extends MongoRepository<FoodEntity,String> {
}
