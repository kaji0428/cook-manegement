package com.example.cookingmanagement.mapper;

import com.example.cookingmanagement.entity.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("INSERT INTO comments (recipe_id, user_id, comment_text, created_at) VALUES (#{recipeId}, #{userId}, #{commentText}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "commentId")
    void insertComment(Comment comment);

    @Select("""
        SELECT c.comment_id, c.recipe_id, c.user_id, c.comment_text, c.created_at,
               u.username
        FROM comments c
        JOIN users u ON c.user_id = u.user_id
        WHERE c.recipe_id = #{recipeId}
        ORDER BY c.created_at DESC
        """)
    @Results({
            @Result(property = "commentId", column = "comment_id"),
            @Result(property = "recipeId", column = "recipe_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "commentText", column = "comment_text"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "user", javaType = com.example.cookingmanagement.entity.User.class, column = "user_id", one = @One(select = "com.example.cookingmanagement.mapper.UserMapper.selectUserById"))
    })
    List<Comment> findCommentsByRecipeId(int recipeId);
}