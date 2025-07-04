package com.example.cookingmanagement.controller;

import com.example.cookingmanagement.entity.Ingredient;
import com.example.cookingmanagement.entity.Recipe;
import com.example.cookingmanagement.form.IngredientForm;
import com.example.cookingmanagement.form.RecipeForm;
import com.example.cookingmanagement.service.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller // コントローラクラスとして認識されるアノテーション
public class RecipeController {

    // サービスクラスのインスタンスを用意
    private final RecipeService recipeService;

    // コンストラクタでDI（依存性注入）
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    /**
     * レシピ一覧画面を表示するメソッド（検索機能付き）
     */
    @GetMapping("/recipes")
    public String showRecipeList(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        List<Recipe> recipes;

        // 検索キーワードが入力されている場合は検索、それ以外は全件取得
        if (keyword != null && !keyword.isEmpty()) {
            recipes = recipeService.searchByTitle(keyword);
            model.addAttribute("keyword", keyword); // 入力内容を画面に保持する
        } else {
            recipes = recipeService.getAllRecipes();
        }

        // レシピ一覧をビューに渡す
        model.addAttribute("recipes", recipes);
        return "recipe-list";
    }

    /**
     * 詳細ページを表示するメソッド
     */
    @GetMapping("/recipes/{id}")
    public String showRecipeDetail(@PathVariable("id") int id, Model model) {
        Recipe recipe = recipeService.getRecipeById(id);

        // 説明文をXSS対策して改行を <br> に変換
        String safeDescription = HtmlUtils.htmlEscape(recipe.getDescription());
        safeDescription = safeDescription.replace("\n", "<br>");

        model.addAttribute("recipe", recipe);
        model.addAttribute("safeDescription", safeDescription); // 安全な説明文として渡す

        return "recipe-detail";
    }

    /**
     * 新規登録フォームの表示
     */
    @GetMapping("/recipes/new")
    public String showCreateForm(Model model) {
        model.addAttribute("recipeForm", new RecipeForm()); // 空のフォームを用意
        return "recipe-form";
    }

    /**
     * 新規レシピを登録する処理（画像の保存も含む）
     */
    @PostMapping("/recipes")
    public String createRecipe(@ModelAttribute RecipeForm recipeForm,
                               @RequestParam("imageFile") MultipartFile imageFile) {
        // 画像ファイルが選択されている場合
        if (!imageFile.isEmpty()) {
            try {
                String uploadDir = "src/main/resources/static/images/"; // 保存先
                String fileName = imageFile.getOriginalFilename(); // ファイル名取得
                Path filePath = Paths.get(uploadDir + fileName);
                Files.copy(imageFile.getInputStream(), filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING); // ファイル保存
                recipeForm.setImagePath("images/" + fileName); // パスをセット
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        recipeService.createRecipe(recipeForm); // サービス経由で登録
        return "redirect:/recipes"; // 一覧画面にリダイレクト
    }

    /**
     * 編集フォームの表示
     */
    @GetMapping("/recipes/edit/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model) {
        Recipe recipe = recipeService.getRecipeById(id);
        if (recipe == null) {
            return "redirect:/recipes"; // 存在しないIDの場合の処理（安全）
        }

        RecipeForm form = recipeService.convertToForm(recipe);

        // 材料を IngredientForm に変換
        List<IngredientForm> ingredientForms = recipe.getIngredients().stream()
                .map(ing -> {
                    IngredientForm f = new IngredientForm();
                    f.setName(ing.getName());
                    f.setQuantity(ing.getQuantity());
                    return f;
                })
                .toList();
        form.setIngredients(ingredientForms);

        model.addAttribute("recipeForm", form);
        model.addAttribute("recipeId", id);
        return "recipe-form";
    }

    /**
     * レシピの更新処理（画像ファイルの再登録も含む）
     */
    @PostMapping("/recipes/update")
    public String updateRecipe(@ModelAttribute RecipeForm recipeForm,
                               @RequestParam(value = "recipeId", required = false) Integer recipeId,
                               @RequestParam("imageFile") MultipartFile imageFile,
                               @RequestParam("originalImagePath") String originalImagePath) {

        // 新しい画像がアップロードされた場合
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String uploadDir = "src/main/resources/static/images/";
                String fileName = imageFile.getOriginalFilename();
                Path path = Paths.get(uploadDir + fileName);
                Files.copy(imageFile.getInputStream(), path, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                recipeForm.setImagePath("images/" + fileName); // 新しい画像パスを設定
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // 新しい画像がなければ、元の画像パスをそのまま使用
            recipeForm.setImagePath(originalImagePath);
        }

        // 更新処理（念のため recipeId が null のときは新規登録に切り替え）
        if (recipeId != null) {
            recipeService.updateRecipe(recipeId, recipeForm);
            return "redirect:/recipes/" + recipeId; // 詳細ページへ
        } else {
            recipeService.createRecipe(recipeForm); // 例外的なケース
            return "redirect:/recipes";
        }
    }

    /**
     * レシピの削除処理
     */
    @PostMapping("/recipes/delete/{id}")
    public String deleteRecipe(@PathVariable("id") int id) {
        recipeService.deleteRecipe(id);
        return "redirect:/recipes";
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
    @GetMapping("/recipes/random")
    public String redirectToRandomRecipe() {
        List<Recipe> allRecipes = recipeService.getAllRecipes(); // 既存のメソッドを使う！
        if (allRecipes.isEmpty()) {
            return "redirect:/recipes"; // レシピが1件もないとき
        }

        Recipe random = allRecipes.get(new Random().nextInt(allRecipes.size()));
        return "redirect:/recipes/" + random.getRecipeId(); // 詳細ページへリダイレクト
    }
    @GetMapping("/recipes/random-id")
    @ResponseBody
    public Map<String, Object> getRandomRecipeId() {
        List<Recipe> allRecipes = recipeService.getAllRecipes();
        Map<String, Object> result = new HashMap<>();

        if (allRecipes.isEmpty()) {
            result.put("id", 0);
        } else {
            Recipe random = allRecipes.get(new Random().nextInt(allRecipes.size()));
            result.put("id", random.getRecipeId());
        }

        return result;
    }


}
