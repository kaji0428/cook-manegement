package com.example.cookingmanagement.controller;

import com.example.cookingmanagement.entity.Ingredient;
import com.example.cookingmanagement.entity.Recipe;
import com.example.cookingmanagement.form.IngredientForm;
import com.example.cookingmanagement.form.RecipeForm;
import com.example.cookingmanagement.service.RecipeService;
import com.example.cookingmanagement.service.CommentService;
import com.example.cookingmanagement.service.GeminiService; // 追加
import com.example.cookingmanagement.form.CommentForm;
import com.example.cookingmanagement.entity.Comment;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // 追加
import org.springframework.security.core.context.SecurityContextHolder; // 追加
import com.example.cookingmanagement.security.CustomUserDetails; // 追加

@Controller // コントローラクラスとして認識されるアノテーション
public class RecipeController {

    // サービスクラスのインスタンスを用意
    private final RecipeService recipeService;
    private final CommentService commentService;
    private final GeminiService geminiService; // GeminiServiceを追加

    // コンストラクタでDI（依存性注入）
    public RecipeController(RecipeService recipeService, CommentService commentService, GeminiService geminiService) {
        this.recipeService = recipeService;
        this.commentService = commentService;
        this.geminiService = geminiService; // 注入
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
        model.addAttribute("pageTitle", "レシピ一覧"); // ページタイトルを設定
        return "recipe-list";
    }

    /**
     * 詳細ページを表示するメソッド
     */
    @GetMapping("/recipes/{id}")
    public String showRecipeDetail(@PathVariable("id") int id, Model model, Principal principal) {
        Recipe recipe = recipeService.getRecipeById(id);
        if (recipe == null) {
            return "redirect:/recipes";
        }

        // 必ず description が null でないようにしつつ改行変換
        String rawDescription = recipe.getDescription() != null ? recipe.getDescription() : "";
        String safeDescription = HtmlUtils.htmlEscape(rawDescription).replace("\n", "<br>");

        // Gemini APIを使って優しい解説を生成
        String gentleDescription = geminiService.getGentleExplanation(rawDescription);
        String safeGentleDescription = HtmlUtils.htmlEscape(gentleDescription).replace("\n", "<br>");

        boolean isOwner = false;
        if (principal != null && recipe.getUser() != null) {
            String loggedInUsername = principal.getName();
            isOwner = recipe.getUser().getUsername().equals(loggedInUsername);
        }

        List<Comment> comments = commentService.getCommentsByRecipeId(id);

        model.addAttribute("recipe", recipe);
        model.addAttribute("safeDescription", safeDescription);
        model.addAttribute("safeGentleDescription", safeGentleDescription); // 優しい解説を追加
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("comments", comments);
        model.addAttribute("commentForm", new CommentForm());

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

        @PostMapping("/recipes/{id}/favorite")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> toggleFavorite(@PathVariable("id") int id) {
        Map<String, Object> result = recipeService.toggleFavorite(id);
        return ResponseEntity.ok(result);
    }

    /**
     * お気に入りレシピ一覧画面を表示するメソッド
     */
    @GetMapping("/recipes/favorites")
    public String showFavoriteRecipes(Model model) {
        List<Recipe> recipes = recipeService.getFavoriteRecipes();
        model.addAttribute("recipes", recipes);
        model.addAttribute("pageTitle", "お気に入り"); // ページタイトルを設定
        return "recipe-list";
    }

    /**
     * 自分のレシピ一覧画面を表示するメソッド
     */
    @GetMapping("/recipes/my")
    public String showMyRecipes(Model model) {
        List<Recipe> recipes = recipeService.getMyRecipes();
        model.addAttribute("recipes", recipes);
        // ログインユーザーのユーザー名を取得してページタイトルに設定
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            model.addAttribute("pageTitle", userDetails.getUsername() + "");
        } else {
            model.addAttribute("pageTitle", "自分のレシピ"); // ログインしていない場合
        }
        return "recipe-list";
    }

    @PostMapping("/recipes/{recipeId}/comments")
    @ResponseBody
    public ResponseEntity<?> addComment(@PathVariable("recipeId") int recipeId,
                                           @Valid @RequestBody CommentForm commentForm,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // エラー内容をMapに詰めて返す
            Map<String, String> fieldErrors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    fieldErrors.put(error.getField(), error.getDefaultMessage())
            );
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Validation failed");
            errorResponse.put("errors", fieldErrors); // errors を Map<String, String> に変更
            return ResponseEntity.badRequest().body(errorResponse);
        }

        commentForm.setRecipeId(recipeId);
        Comment newComment = commentService.addComment(commentForm); // 保存されたコメントエンティティを受け取る

        // 成功レスポンスに必要な情報をMapに詰めて返す
        Map<String, Object> response = new HashMap<>();
        response.put("commentText", newComment.getCommentText());
        response.put("username", newComment.getUser().getUsername()); // ユーザー名を追加
        response.put("createdAt", newComment.getCreatedAt().toString()); // 登録日時を追加

        return ResponseEntity.ok(response);
    }
}
