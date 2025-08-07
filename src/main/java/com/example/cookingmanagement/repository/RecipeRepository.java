package com.example.cookingmanagement.repository;

import com.example.cookingmanagement.entity.Recipe;
import com.example.cookingmanagement.mapper.RecipeMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RecipeRepository {

    private final RecipeMapper recipeMapper;

    public RecipeRepository(RecipeMapper recipeMapper) {
        this.recipeMapper = recipeMapper;
    }

    public List<Recipe> findAll() {
        return recipeMapper.findAll();
    }

    public Recipe findById(int id) {
        return recipeMapper.findById(id);
    }

    public void insert(Recipe recipe) {
        recipeMapper.insert(recipe);
    }

    public void update(Recipe recipe) {
        recipeMapper.update(recipe);
    }

    public void deleteById(int id) {
        recipeMapper.deleteById(id);
    }

    public List<Recipe> findByTitleLike(String title) {
        return recipeMapper.findByTitleLike(title);
    }

    public void insertFavorite(int userId, int recipeId) {
        recipeMapper.insertFavorite(userId, recipeId);
    }

    public void deleteFavorite(int userId, int recipeId) {
        recipeMapper.deleteFavorite(userId, recipeId);
    }

    public int countFavorite(int userId, int recipeId) {
        return recipeMapper.countFavorite(userId, recipeId);
    }

    public int countFavoriteByRecipeId(int recipeId) {
        return recipeMapper.countFavoriteByRecipeId(recipeId);
    }

    public void deleteFavoritesByRecipeId(int recipeId) {
        recipeMapper.deleteFavoritesByRecipeId(recipeId);
    }

    public void deleteCommentsByRecipeId(int recipeId) {
        recipeMapper.deleteCommentsByRecipeId(recipeId);
    }

    public List<Recipe> findFavoriteRecipesByUserId(int userId) {
        return recipeMapper.findFavoriteRecipesByUserId(userId);
    }

    public List<Recipe> findRecipesByUserId(int userId) {
        return recipeMapper.findRecipesByUserId(userId);
    }
}