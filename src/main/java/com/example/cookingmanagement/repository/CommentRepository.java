package com.example.cookingmanagement.repository;

import com.example.cookingmanagement.entity.Comment;
import com.example.cookingmanagement.mapper.CommentMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepository {

    private final CommentMapper commentMapper;

    public CommentRepository(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    public void insertComment(Comment comment) {
        commentMapper.insertComment(comment);
    }

    public List<Comment> findCommentsByRecipeId(int recipeId) {
        return commentMapper.findCommentsByRecipeId(recipeId);
    }
}
