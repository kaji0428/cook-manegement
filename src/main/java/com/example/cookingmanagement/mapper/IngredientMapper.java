package com.example.cookingmanagement.mapper;

import com.example.cookingmanagement.entity.Ingredient;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IngredientMapper {

    // 特定のレシピに紐づく材料一覧を取得
    @Select("SELECT * FROM ingredients WHERE recipe_id = #{recipeId}")
    List<Ingredient> findIngredientsByRecipeId(int recipeId);

    // 材料を登録（エンティティを使うバージョン）
    @Insert("INSERT INTO ingredients (name, quantity, recipe_id, created_at) " +
            "VALUES (#{name}, #{quantity}, #{recipeId}, CURRENT_TIMESTAMP)")
    void insertIngredient(@Param("name") String name,
                          @Param("quantity") String quantity,
                          @Param("recipeId") int recipeId);

    // 材料を一括削除（レシピID指定）
    @Delete("DELETE FROM ingredients WHERE recipe_id = #{recipeId}")
    void deleteByRecipeId(int recipeId);
}
