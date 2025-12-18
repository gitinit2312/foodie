package com.jspider.foodiesapi.service;

import com.jspider.foodiesapi.entity.UserEntity;
import com.jspider.foodiesapi.io.UserRequest;
import com.jspider.foodiesapi.io.UserResponse;
import com.jspider.foodiesapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepo;
    private final AuthanticationFacade authanticationFacade;
    @Override
    public UserResponse registerUser(UserRequest userReq) {
        UserEntity user = convertToEntity(userReq);
        return convertToResponse(userRepo.save(user));
    }

    @Override
    public String UserById() {
        String email = authanticationFacade.getAuthentication().getName();
         UserEntity user = userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
         return user.getId();
    }

    private UserEntity convertToEntity(UserRequest user){
        return UserEntity.builder()
                .email(user.getEmail())
                .name(user.getName())
                .password(passwordEncoder.encode(user.getPassword()))
                .build();
    }

    private UserResponse convertToResponse(UserEntity user){
        return UserResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .id(user.getId())
                .build();
    }
}
