package com.example.cookingmanagement.service;

import com.example.cookingmanagement.entity.Ingredient;
import com.example.cookingmanagement.entity.Recipe;
import com.example.cookingmanagement.form.IngredientForm;
import com.example.cookingmanagement.form.RecipeForm;
import com.example.cookingmanagement.mapper.IngredientMapper;
import com.example.cookingmanagement.mapper.RecipeConvertMapper;
import com.example.cookingmanagement.repository.RecipeRepository;
import com.example.cookingmanagement.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeConvertMapper recipeConvertMapper;
    private final IngredientMapper ingredientMapper;

    public RecipeService(RecipeRepository recipeRepository, RecipeConvertMapper recipeConvertMapper, IngredientMapper ingredientMapper) {
        this.recipeRepository = recipeRepository;
        this.recipeConvertMapper = recipeConvertMapper;
        this.ingredientMapper = ingredientMapper;
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = recipeRepository.findAll();
        Integer userId = getLoginUserId();
        if (userId != null) {
            for (Recipe recipe : recipes) {
                recipe.setFavorited(recipeRepository.countFavorite(userId, recipe.getRecipeId()) > 0);
            }
        }
        return recipes;
    }

    public Recipe getRecipeById(int id) {
        Recipe recipe = recipeRepository.findById(id);
        if (recipe == null) {
            return null; // 呼び出し元で null を確認する
        }

        recipe.setIngredients(ingredientMapper.findIngredientsByRecipeId(id));
        Integer userId = getLoginUserId();
        if (userId != null) {
            recipe.setFavorited(recipeRepository.countFavorite(userId, recipe.getRecipeId()) > 0);
        }
        recipe.setFavoriteCount(recipeRepository.countFavoriteByRecipeId(id));
        return recipe;
    }

    public void createRecipe(RecipeForm form) {
        Recipe recipe = recipeConvertMapper.toEntity(form);
        recipe.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        // 🔽 ログインユーザーを取得して、レシピに投稿者をセット
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails userDetails) {
            recipe.setUser(userDetails.getUser());
        }

        // レシピを保存（投稿者付き！）
        recipeRepository.insert(recipe);

        int recipeId = recipe.getRecipeId(); // 自動採番されたID

        // 材料を保存
        for (IngredientForm ingredientForm : form.getIngredients()) {
            if (ingredientForm.getName() != null && !ingredientForm.getName().isBlank()) {
                ingredientMapper.insertIngredient(
                        ingredientForm.getName(),
                        ingredientForm.getQuantity(),
                        recipeId
                );
            }
        }
    }

    public void updateRecipe(int id, RecipeForm form) {
        Recipe recipe = recipeConvertMapper.toEntity(form);
        recipe.setRecipeId(id);
        recipe.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        // レシピ情報の更新
        recipeRepository.update(recipe);

        // 材料を一旦すべて削除してから、再挿入
        ingredientMapper.deleteByRecipeId(id);

        // 🔽 RecipeFormから材料を取得して保存
        if (form.getIngredients() != null) {
            for (IngredientForm ingredientForm : form.getIngredients()) {
                if (ingredientForm.getName() != null && !ingredientForm.getName().isBlank()) {
                    ingredientMapper.insertIngredient(
                            ingredientForm.getName(),
                            ingredientForm.getQuantity(),
                            id
                    );
                }
            }
        }
    }

    public RecipeForm convertToForm(Recipe recipe) {
        RecipeForm form = recipeConvertMapper.toForm(recipe);

        // レシピに紐づく材料を IngredientForm に変換してセット
        if (recipe.getIngredients() != null) {
            form.setIngredients(convertIngredientsToForm(recipe.getIngredients()));
        }

        return form;
    }

    public void deleteRecipe(int id) {
        // 材料を先に削除してからレシピを削除
        ingredientMapper.deleteByRecipeId(id);
        recipeRepository.deleteById(id);
    }

    public List<Recipe> searchByTitle(String keyword) {
        return recipeRepository.findByTitleLike("%" + keyword + "%");
    }

    public Map<String, Object> toggleFavorite(int recipeId) {
        Integer userId = getLoginUserId();
        Map<String, Object> result = new HashMap<>();

        if (userId == null) {
            result.put("favorited", false);
            result.put("favoriteCount", recipeRepository.countFavoriteByRecipeId(recipeId));
            return result;
        }

        boolean isFavorited = recipeRepository.countFavorite(userId, recipeId) > 0;
        if (isFavorited) {
            recipeRepository.deleteFavorite(userId, recipeId);
        } else {
            recipeRepository.insertFavorite(userId, recipeId);
        }

        result.put("favorited", !isFavorited);
        result.put("favoriteCount", recipeRepository.countFavoriteByRecipeId(recipeId));
        return result;
    }

    public List<Recipe> getFavoriteRecipes() {
        Integer userId = getLoginUserId();
        if (userId == null) {
            return new ArrayList<>();
        }
        List<Recipe> recipes = recipeRepository.findFavoriteRecipesByUserId(userId);
        // お気に入り状態を設定（すべてお気に入りなのでtrue）
        for (Recipe recipe : recipes) {
            recipe.setFavorited(true);
        }
        return recipes;
    }

    public List<Recipe> getMyRecipes() {
        Integer userId = getLoginUserId();
        if (userId == null) {
            return new ArrayList<>();
        }
        List<Recipe> recipes = recipeRepository.findRecipesByUserId(userId);
        // 各レシピのお気に入り状態を設定
        for (Recipe recipe : recipes) {
            recipe.setFavorited(recipeRepository.countFavorite(userId, recipe.getRecipeId()) > 0);
        }
        return recipes;
    }

    private Integer getLoginUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getUserId();
        }
        return null;
    }

    private List<IngredientForm> convertIngredientsToForm(List<Ingredient> ingredients) {
        List<IngredientForm> forms = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            IngredientForm form = new IngredientForm();
            form.setName(ingredient.getName());
            form.setQuantity(ingredient.getQuantity());
            forms.add(form);
        }
        return forms;
    }
}