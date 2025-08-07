package com.example.cookingmanagement.service;

import com.example.cookingmanagement.entity.Comment;
import com.example.cookingmanagement.form.CommentForm;
import com.example.cookingmanagement.repository.CommentRepository;
import com.example.cookingmanagement.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;

    public CommentService(CommentRepository commentRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
    }

    public Comment addComment(CommentForm form) {
        Comment comment = new Comment();
        comment.setRecipeId(form.getRecipeId());
        comment.setCommentText(form.getCommentText());
        comment.setCreatedAt(LocalDateTime.now());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails userDetails) {
            comment.setUserId(userDetails.getUserId());
        } else {
            throw new IllegalStateException("ログインユーザーが見つかりません");
        }

        commentRepository.insertComment(comment);
        // 保存後にユーザー情報を取得してCommentオブジェクトにセット
        comment.setUser(userService.getUserById(comment.getUserId()));
        return comment;
    }

    public List<Comment> getCommentsByRecipeId(int recipeId) {
        return commentRepository.findCommentsByRecipeId(recipeId);
    }
}
