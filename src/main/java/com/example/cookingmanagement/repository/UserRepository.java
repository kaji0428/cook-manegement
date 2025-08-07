package com.example.cookingmanagement.repository;

import com.example.cookingmanagement.mapper.UserMapper;
import com.example.cookingmanagement.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private final UserMapper userMapper;
    public UserRepository(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    public User selectUserByUsername(String username) {
        return userMapper.selectUserByUsername(username);
    }
    public void insertUser(User user) {
        userMapper.insertUser(user);
    }

    public User selectUserById(int userId) {
        return userMapper.selectUserById(userId);
    }
}
