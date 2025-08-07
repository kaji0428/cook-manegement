package com.example.cookingmanagement.entity;

import lombok.Data;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;

@Data
public class User {
    private int userId;

    @NotBlank(message = "ユーザ名は必須です")
    private String username;

    @NotBlank(message = "パスワードは必須です")
    private String password;

    private LocalDateTime createdAt;
}
