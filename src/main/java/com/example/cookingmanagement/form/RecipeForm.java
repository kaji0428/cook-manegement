package com.example.cookingmanagement.form;

import java.util.List;
import java.util.ArrayList;

/**
 * レシピ登録・編集フォームで使用するフォームクラス。
 * ユーザーが入力した値を受け取って、一時的に保持します。
 */
public class RecipeForm {

    // 料理のタイトル（例：オムライス）
    private String title;

    // キャッチコピー（例：ふわとろ卵が絶品！）
    private String shortDescription;

    // レシピの詳細な説明文
    private String description;

    // アップロードされた画像ファイルのパス（"images/xxx.jpg" 形式）
    private String imagePath;

    // 材料のリスト（IngredientForm型で複数保持）
    private List<IngredientForm> ingredients = new ArrayList<>();

    // --- Getter / Setter ---

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public List<IngredientForm> getIngredients() {
        return ingredients;
    }
    public void setIngredients(List<IngredientForm> ingredients) {
        this.ingredients = ingredients;
    }
}
