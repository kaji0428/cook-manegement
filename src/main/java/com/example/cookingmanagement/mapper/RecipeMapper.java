package com.example.cookingmanagement.mapper;

import com.example.cookingmanagement.entity.Recipe;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RecipeMapper {

    @Select("SELECT recipe_id, title, short_description, description, image_path, created_at FROM recipes")
    List<Recipe> findAll();

    @Select("SELECT recipe_id, title, short_description, description, image_path, created_at FROM recipes WHERE recipe_id = #{id}")
    Recipe findById(int id);

    // ✅ 主キー自動取得の設定（重複を削除）
    @Insert("INSERT INTO recipes (title, short_description, description, image_path, created_at) " +
            "VALUES (#{title}, #{shortDescription}, #{description}, #{imagePath}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "recipeId")
    void insert(Recipe recipe);

    @Update("UPDATE recipes SET title = #{title}, short_description = #{shortDescription}, " +
            "description = #{description}, image_path = #{imagePath}, created_at = #{createdAt} WHERE recipe_id = #{recipeId}")
    void update(Recipe recipe);

    @Delete("DELETE FROM recipes WHERE recipe_id = #{id}")
    void deleteById(int id);

    @Select("SELECT * FROM recipes WHERE title LIKE #{title}")
    List<Recipe> findByTitleLike(String title);

    @Insert("INSERT INTO favorites (user_id, recipe_id) VALUES (#{userId}, #{recipeId})")
    void insertFavorite(@Param("userId") int userId, @Param("recipeId") int recipeId);

    @Delete("DELETE FROM favorites WHERE user_id = #{userId} AND recipe_id = #{recipeId}")
    void deleteFavorite(@Param("userId") int userId, @Param("recipeId") int recipeId);

    @Select("SELECT COUNT(*) FROM favorites WHERE user_id = #{userId} AND recipe_id = #{recipeId}")
    int countFavorite(@Param("userId") int userId, @Param("recipeId") int recipeId);

    @Select("SELECT COUNT(*) FROM favorites WHERE recipe_id = #{recipeId}")
    int countFavoriteByRecipeId(@Param("recipeId") int recipeId);
}
