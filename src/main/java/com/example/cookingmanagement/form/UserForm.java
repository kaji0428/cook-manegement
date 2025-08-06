package com.example.cookingmanagement.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserForm {
    @NotBlank(message = "ユーザー名は必須です")
    private String username;

    @NotBlank(message = "パスワードは必須です")
    @Size(min = 8, message = "パスワードは8文字以上で入力してください")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).*$", message = "パスワードは英数字混合で入力してください")
    private String password;

    // Additional fields can be added as needed
}
