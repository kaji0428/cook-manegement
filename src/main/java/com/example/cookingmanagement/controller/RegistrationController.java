package com.example.cookingmanagement.controller;
import com.example.cookingmanagement.form.UserForm;
import com.example.cookingmanagement.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    private final UserService userService;
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "register";
    }
    @PostMapping("/register")
    public String registerUser(@Validated UserForm userForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userForm", userForm);
            return "register";
        }
        try {
            userService.createUser(userForm);
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("username", null, e.getMessage());
            model.addAttribute("userForm", userForm);
            return "register";
        }
        return "redirect:/login?register";
    }
}