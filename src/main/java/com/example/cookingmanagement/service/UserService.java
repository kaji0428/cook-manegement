package com.example.cookingmanagement.service;
import com.example.cookingmanagement.entity.User;
import com.example.cookingmanagement.form.UserForm;
import com.example.cookingmanagement.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public void createUser(UserForm userForm) {
        if (userRepository.selectUserByUsername(userForm.getUsername()) != null) {
            throw new IllegalArgumentException("このユーザー名は既に使われています");
        }
        User user = new User();
        user.setUsername(userForm.getUsername());
        String hashedPassword = passwordEncoder.encode(userForm.getPassword());
        user.setPassword(hashedPassword);
        userRepository.insertUser(user);
    }

    public User getUserById(int userId) {
        return userRepository.selectUserById(userId);
    }
}
