package com.example.cookingmanagement.entity;

import java.sql.Timestamp;
import java.util.List;
import com.example.cookingmanagement.entity.Ingredient;

/**
 * レシピ情報を表すエンティティクラス。
 * DBの `recipes` テーブルと対応します。
 */
public class Recipe {

    // レシピID（主キー）
    private int recipeId;

    // レシピのタイトル（料理名）
    private String title;

    // 短い説明（キャッチコピーなど）
    private String shortDescription;

    // 詳細な説明（レシピの手順など）
    private String description;

    // 画像ファイルのパス（例：images/omurice.jpg）
    private String imagePath;

    // 登録日時（タイムスタンプ）
    private Timestamp createdAt;

    // このレシピが現在のユーザーにお気に入り登録されているか
    private boolean favorited;

    // お気に入り数
    private int favoriteCount;

    // 材料リスト（このレシピに紐づく材料たち）
    private List<Ingredient> ingredients;

    // 投稿者（ログインユーザー）
    private User user;

    // --- Getter / Setter ---

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
