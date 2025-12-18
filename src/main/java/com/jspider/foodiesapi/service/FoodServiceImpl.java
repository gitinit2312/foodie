package com.jspider.foodiesapi.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.jspider.foodiesapi.entity.FoodEntity;
import com.jspider.foodiesapi.io.FoodRequest;
import com.jspider.foodiesapi.io.FoodResponse;
import com.jspider.foodiesapi.repository.FoodRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService{
	
private final FoodRepo foodRepository;
private final S3Client s3Client;

@Value("${aws.s3.bucket}")
private String bucketName;


@Override
public String uploadFile(MultipartFile file) {
  String filenameExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+ 1);
 String key =  UUID.randomUUID().toString()+"." + filenameExtension;
 
 try {
	 PutObjectRequest putObjectRequest = PutObjectRequest.builder()
			 .bucket(bucketName)
			 .key(key)
			 .acl("public-read")
			 .contentType(file.getContentType())
			 .build();
	 PutObjectResponse response = s3Client.putObject(putObjectRequest , RequestBody.fromBytes(file.getBytes()));
 
	 if(response.sdkHttpResponse().isSuccessful()) {
		 return "https://" + bucketName + ".s3.ap-south-2.amazonaws.com/" + key;
	 }else {
		 throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR ,"file uploadFailed" );
	 }

 }catch(IOException e) {
	throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR ,"error occured while uploading the file to ");
 }
}

	@Override
	public FoodResponse addFood(FoodRequest foodReq, MultipartFile file) {
        FoodEntity food =  convertToEntity(foodReq);
		String imageUrl = uploadFile(file);
		food.setImageUrl(imageUrl);
		food = foodRepository.save(food);
		return convertToResponse(food);
	}

	@Override
	public List<FoodResponse> getFood() {
		List<FoodEntity> databasentry =  foodRepository.findAll();
		return databasentry.stream().map(this::convertToResponse).collect(Collectors.toList());
	}

	@Override
	public FoodResponse readFood(String id) {
      FoodEntity existingFood = foodRepository.findById(id).orElseThrow(() -> new RuntimeException("food not found for the id :" + id));
	  return convertToResponse(existingFood);
	}

	@Override
	public boolean deleteFile(String filename) {
		DeleteObjectRequest deleteobjReq =  DeleteObjectRequest.builder()
				.bucket(bucketName)
				.key(filename)
				.build();
		s3Client.deleteObject(deleteobjReq);
		return true;
	}

	@Override
	public void deleteFood(String id) {
		FoodResponse food = readFood(id);
		String imgUrl = food.getImageUrl();
		String fileName = imgUrl.substring(imgUrl.lastIndexOf("/")+1);
		boolean isDeleted = deleteFile(fileName);
		if(isDeleted){
			foodRepository.deleteById(food.getId());
		}

		}


	private FoodEntity convertToEntity(FoodRequest request){
				return	FoodEntity.builder()
							.name(request.getName())
							.description(request.getDescription())
							.category(request.getCategory())
							.price(request.getPrice())
							.build();
	}

	private FoodResponse convertToResponse(FoodEntity entity){
		return	FoodResponse.builder()
				.id(entity.getId())
				.name(entity.getName())
				.category(entity.getCategory())
				.description(entity.getDescription())
				.price(entity.getPrice())
				.imageUrl(entity.getImageUrl())
				.build();
	}

}
