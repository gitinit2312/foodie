package com.jspider.foodiesapi.io;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String id;
    private String  name;
    private String  email;
}
