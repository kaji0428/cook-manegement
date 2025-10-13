# Cooking Management（料理管理アプリ）

料理レシピの登録・検索・表示、画像アップロード、コメント・お気に入り、ログイン管理などを行う Spring Boot アプリケーションです。  
UI は Thymeleaf によるサーバーサイドレンダリング（SSR）で構成し、一部は JSON API で非同期連携を行っています。  
データアクセスは Spring Data JPA ではなく MyBatis を採用しています。

## デモ動画
- [YouTube - Demo](https://www.youtube.com/watch?v=4VH3UoxWjGk)

## 主な機能

- レシピ
  - 一覧・詳細表示、検索（タイトル部分一致）
  - 新規作成・編集・削除（画像アップロード対応）
  - ランダム表示（/recipes/random, /recipes/random-id）
- コメント
  - レシピ詳細へのコメント投稿（バリデーションあり、JSON で返却）
- お気に入り
  - お気に入り切替（トグル）・お気に入り一覧表示
- 認証/認可
  - ログイン・ユーザー登録、BCrypt でのパスワードハッシュ
  - 未認証時は閲覧のみ、作成/編集/削除は認証必須
- 外部 API 連携
  - Gemini API を用いた「やさしい解説」生成（非同期 JSON API）

代表的な画面・エンドポイント
- 画面（Thymeleaf）
  - GET /recipes（一覧・検索）
  - GET /recipes/{id}（詳細）
  - GET /recipes/new（作成フォーム）
  - GET /recipes/edit/{id}（編集フォーム）
  - POST /recipes（作成）
  - POST /recipes/update（更新）
  - POST /recipes/delete/{id}（削除）
  - GET /recipes/favorites（お気に入り一覧）
  - GET /recipes/my（自分のレシピ）
  - GET /login（ログイン）
- JSON API
  - GET /api/recipes/{id}/explanation（Gemini による解説生成）
  - POST /recipes/{recipeId}/comments（コメント投稿）
  - POST /recipes/{id}/favorite（お気に入りトグル）
  - GET /recipes/random-id（ランダム ID 取得）

## 技術スタック

- 言語/フレームワーク
  - Java, Spring Boot 3.x（Maven ビルド）
  - Spring MVC + Thymeleaf（Ultraq Layout Dialect、Thymeleaf Extras Spring Security 6）
  - Spring Security（フォームログイン、BCrypt）
- データアクセス
  - MyBatis（アノテーションベースの SQL、@Mapper）
- HTTP/外部連携
  - RestTemplate（Gemini API 連携）
- その他
  - Jakarta Validation（@Valid + BindingResult）
  - Jackson（JSON 変換）

## Spring/Spring Boot で利用している機能（確定）

- Spring Boot 基盤
  - @SpringBootApplication による自動設定とコンポーネントスキャン
  - @MapperScan による MyBatis Mapper スキャン
  - 参照: [CookingManagementApplication.java](https://github.com/kaji0428/cook-manegement/blob/master/src/main/java/com/example/cookingmanagement/CookingManagementApplication.java)
- Spring MVC（Web）
  - @Controller, @GetMapping, @PostMapping, Model, ResponseEntity, @ResponseBody
  - 参照: [RecipeController.java](https://github.com/kaji0428/cook-manegement/blob/master/src/main/java/com/example/cookingmanagement/controller/RecipeController.java), [LoginController.java](https://github.com/kaji0428/cook-manegement/blob/master/src/main/java/com/example/cookingmanagement/controller/LoginController.java)
- テンプレート（Thymeleaf）
  - Thymeleaf + Layout Dialect、CSRF メタタグ出力、Security Dialect（sec:）
  - 参照: [templates/layout.html](https://github.com/kaji0428/cook-manegement/blob/master/src/main/resources/templates/layout.html)
- 入力バリデーション
  - @Valid と BindingResult によるフォーム検証（コメント投稿）
  - 参照: [RecipeController.java（addComment）](https://github.com/kaji0428/cook-manegement/blob/master/src/main/java/com/example/cookingmanagement/controller/RecipeController.java)
- Spring Security
  - @EnableWebSecurity + SecurityFilterChain 構成、フォームログイン、アクセス制御、BCryptPasswordEncoder
  - SecurityContextHolder から認証ユーザーを取得してビジネスロジックに反映（投稿者紐付け、マイページ表示）
  - 参照: [SecurityConfig.java](https://github.com/kaji0428/cook-manegement/blob/master/src/main/java/com/example/cookingmanagement/security/SecurityConfig.java)
- データアクセス（MyBatis）
  - @Mapper（アノテーション SQL）、Repository クラスからの呼び出し
  - 参照: [IngredientMapper.java](https://github.com/kaji0428/cook-manegement/blob/master/src/main/java/com/example/cookingmanagement/mapper/IngredientMapper.java), [UserMapper.java](https://github.com/kaji0428/cook-manegement/blob/master/src/main/java/com/example/cookingmanagement/mapper/UserMapper.java), [CommentMapper.java](https://github.com/kaji0428/cook-manegement/blob/master/src/main/java/com/example/cookingmanagement/mapper/CommentMapper.java), [RecipeRepository.java](https://github.com/kaji0428/cook-manegement/blob/master/src/main/java/com/example/cookingmanagement/repository/RecipeRepository.java)
- HTTP クライアント/外部 API
  - RestTemplate による POST 呼び出し、@Value による API キー注入
  - 参照: [GeminiService.java](https://github.com/kaji0428/cook-manegement/blob/master/src/main/java/com/example/cookingmanagement/service/GeminiService.java)
- テスト
  - @SpringBootTest によるコンテキストロード
  - 参照: [CookManagementApplicationTests.java](https://github.com/kaji0428/cook-manegement/blob/master/src/test/java/com/example/cookingmanagement/CookManagementApplicationTests.java)

## ディレクトリ構成（抜粋）

- controller … 画面/JSON エンドポイント（例: RecipeController, LoginController）
- service … ビジネスロジック（例: RecipeService, UserService, GeminiService）
- repository … 永続化層（MyBatis Mapper のラッパ）
- mapper … MyBatis の @Mapper インターフェース（アノテーション SQL）
- security … Spring Security 設定と UserDetails 関連
- templates … Thymeleaf テンプレート
- static … CSS/画像 等の静的リソース

## セキュリティ（要点）

- 未認証で許可: /css/**, /images/**, /kami.mp3, /login, /logout, /register
- 認証必須: /recipes/new, /recipes/edit/**, /recipes/update, /recipes/delete/**, および /recipes/** の大半
- ログイン成功後の遷移: /recipes
- パスワード: BCrypt（PasswordEncoder）
- CSRF: 静的配下やコメント API など一部エンドポイントは除外設定、テンプレート側で CSRF メタタグ出力

詳細: [SecurityConfig.java](https://github.com/kaji0428/cook-manegement/blob/master/src/main/java/com/example/cookingmanagement/security/SecurityConfig.java)

## セットアップ

前提
- JDK 17+（推奨）
- Maven
- RDBMS（アプリ設定に依存。MyBatis の接続先を application.properties/yml に設定）

環境設定（例）
- application.properties（例）
  - spring.datasource.url, spring.datasource.username, spring.datasource.password
  - gemini.api.key（GeminiService 用）
- プロファイルは任意（local/dev 等）

起動
```bash
# 開発起動
mvn spring-boot:run

# ビルド
mvn clean package
```
