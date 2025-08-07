package com.example.cookingmanagement.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Comment {
    private int commentId;
    private int recipeId;
    private int userId;
    private String commentText;
    private LocalDateTime createdAt;
    private User user; // コメント投稿者の情報を保持
}
