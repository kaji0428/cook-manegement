package com.example.cookingmanagement.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentForm {
    private int recipeId;

    @NotBlank(message = "コメントは必須です")
    @Size(max = 1000, message = "コメントは1000文字以内で入力してください")
    private String commentText;
}
