package com.example.cookingmanagement.mapper;

import com.example.cookingmanagement.entity.Recipe;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RecipeMapper {

    @Select("""
        SELECT r.recipe_id, r.title, r.short_description, r.description, r.image_path, r.created_at,
               r.user_id, u.username
        FROM recipes r
        LEFT JOIN users u ON r.user_id = u.user_id
        """)
    @Results({
            @Result(property = "recipeId", column = "recipe_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "shortDescription", column = "short_description"),
            @Result(property = "description", column = "description"),
            @Result(property = "imagePath", column = "image_path"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "user.userId", column = "user_id"),
            @Result(property = "user.username", column = "username")
    })
    List<Recipe> findAll();

    @Select("""
        SELECT r.recipe_id, r.title, r.short_description, r.description, r.image_path, r.created_at,
               r.user_id, u.username
        FROM recipes r
        LEFT JOIN users u ON r.user_id = u.user_id
        WHERE r.recipe_id = #{id}
        """)
    @Results({
            @Result(property = "recipeId", column = "recipe_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "shortDescription", column = "short_description"),
            @Result(property = "description", column = "description"),
            @Result(property = "imagePath", column = "image_path"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "user.userId", column = "user_id"),
            @Result(property = "user.username", column = "username")
    })
    Recipe findById(int id);

    @Insert("INSERT INTO recipes (title, short_description, description, image_path, created_at, user_id) " +
            "VALUES (#{title}, #{shortDescription}, #{description}, #{imagePath}, #{createdAt}, #{user.userId})")
    @Options(useGeneratedKeys = true, keyProperty = "recipeId")
    void insert(Recipe recipe);

    @Update("UPDATE recipes SET title = #{title}, short_description = #{shortDescription}, " +
            "description = #{description}, image_path = #{imagePath}, created_at = #{createdAt} WHERE recipe_id = #{recipeId}")
    void update(Recipe recipe);

    @Delete("DELETE FROM recipes WHERE recipe_id = #{id}")
    void deleteById(int id);

    @Select("""
        SELECT r.recipe_id, r.title, r.short_description, r.description, r.image_path, r.created_at,
               r.user_id, u.username
        FROM recipes r
        LEFT JOIN users u ON r.user_id = u.user_id
        WHERE r.title LIKE #{title}
        """)
    @Results({
            @Result(property = "recipeId", column = "recipe_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "shortDescription", column = "short_description"),
            @Result(property = "description", column = "description"),
            @Result(property = "imagePath", column = "image_path"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "user.userId", column = "user_id"),
            @Result(property = "user.username", column = "username")
    })
    List<Recipe> findByTitleLike(String title);

    @Insert("INSERT INTO favorites (user_id, recipe_id) VALUES (#{userId}, #{recipeId})")
    void insertFavorite(@Param("userId") int userId, @Param("recipeId") int recipeId);

    @Delete("DELETE FROM favorites WHERE user_id = #{userId} AND recipe_id = #{recipeId}")
    void deleteFavorite(@Param("userId") int userId, @Param("recipeId") int recipeId);

    @Select("SELECT COUNT(*) FROM favorites WHERE user_id = #{userId} AND recipe_id = #{recipeId}")
    int countFavorite(@Param("userId") int userId, @Param("recipeId") int recipeId);

    @Select("SELECT COUNT(*) FROM favorites WHERE recipe_id = #{recipeId}")
    int countFavoriteByRecipeId(@Param("recipeId") int recipeId);

    @Delete("DELETE FROM favorites WHERE recipe_id = #{recipeId}")
    void deleteFavoritesByRecipeId(@Param("recipeId") int recipeId);

    @Delete("DELETE FROM comments WHERE recipe_id = #{recipeId}")
    void deleteCommentsByRecipeId(@Param("recipeId") int recipeId);

    @Select("""
        SELECT r.recipe_id, r.title, r.short_description, r.description, r.image_path, r.created_at,
               r.user_id, u.username
        FROM recipes r
        JOIN favorites f ON r.recipe_id = f.recipe_id
        LEFT JOIN users u ON r.user_id = u.user_id
        WHERE f.user_id = #{userId}
        """)
    @Results({
            @Result(property = "recipeId", column = "recipe_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "shortDescription", column = "short_description"),
            @Result(property = "description", column = "description"),
            @Result(property = "imagePath", column = "image_path"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "user.userId", column = "user_id"),
            @Result(property = "user.username", column = "username")
    })
    List<Recipe> findFavoriteRecipesByUserId(int userId);

    @Select("""
        SELECT r.recipe_id, r.title, r.short_description, r.description, r.image_path, r.created_at,
               r.user_id, u.username
        FROM recipes r
        LEFT JOIN users u ON r.user_id = u.user_id
        WHERE r.user_id = #{userId}
        """)
    @Results({
            @Result(property = "recipeId", column = "recipe_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "shortDescription", column = "short_description"),
            @Result(property = "description", column = "description"),
            @Result(property = "imagePath", column = "image_path"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "user.userId", column = "user_id"),
            @Result(property = "user.username", column = "username")
    })
    List<Recipe> findRecipesByUserId(int userId);
}
