package com.example.cookingmanagement.mapper;
import com.example.cookingmanagement.entity.User;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface UserMapper {

    // ユーザー名で既存ユーザーを検索（重複チェック用）
    @Select("SELECT user_id, username, password, created_at FROM users WHERE username = #{username}")
    User selectUserByUsername(String username);

    // ユーザー登録（user_id は自動採番）
    @Insert("INSERT INTO users (username, password) VALUES (#{username}, #{password})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    void insertUser(User user);
}

