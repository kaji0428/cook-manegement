package com.example.cookingmanagement.mapper;

import com.example.cookingmanagement.entity.Ingredient;
import com.example.cookingmanagement.entity.Recipe;
import com.example.cookingmanagement.form.IngredientForm;
import com.example.cookingmanagement.form.RecipeForm;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RecipeConvertMapper {

    public Recipe toEntity(RecipeForm form) {
        Recipe recipe = new Recipe();
        recipe.setTitle(form.getTitle());
        recipe.setShortDescription(form.getShortDescription());
        recipe.setDescription(form.getDescription());
        recipe.setImagePath(form.getImagePath());

        // フォームの材料をエンティティに変換して設定
        List<Ingredient> ingredients = new ArrayList<>();
        for (IngredientForm ingForm : form.getIngredients()) {
            Ingredient ing = new Ingredient();
            ing.setName(ingForm.getName());
            ing.setQuantity(ingForm.getQuantity());
            ingredients.add(ing);
        }
        recipe.setIngredients(ingredients);

        return recipe;
    }

    public RecipeForm toForm(Recipe recipe) {
        RecipeForm form = new RecipeForm();
        form.setTitle(recipe.getTitle());
        form.setShortDescription(recipe.getShortDescription());
        form.setDescription(recipe.getDescription());
        form.setImagePath(recipe.getImagePath());

        // エンティティの材料をフォームに変換して設定
        List<IngredientForm> ingredientForms = new ArrayList<>();
        if (recipe.getIngredients() != null) {
            for (Ingredient ing : recipe.getIngredients()) {
                IngredientForm ingForm = new IngredientForm();
                ingForm.setName(ing.getName());
                ingForm.setQuantity(ing.getQuantity());
                ingredientForms.add(ingForm);
            }
        }
        form.setIngredients(ingredientForms);

        return form;
    }
}
