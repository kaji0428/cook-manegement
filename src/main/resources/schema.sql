CREATE TABLE users (
                    user_id INTEGER AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(255) UNIQUE NOT NULL,
                    password VARCHAR(60) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE recipes (
                      recipe_id INTEGER AUTO_INCREMENT PRIMARY KEY,
                      title VARCHAR(255) NOT NULL,
                      short_description VARCHAR(255),
                      description VARCHAR(1000),
                      image_path VARCHAR(255),
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      user_id INTEGER, -- ← 追加！
                      FOREIGN KEY (user_id) REFERENCES users(user_id)
);
CREATE TABLE ingredients (
 ingredient_id INTEGER AUTO_INCREMENT PRIMARY KEY,
 name VARCHAR(255) NOT NULL,
 quantity VARCHAR(100),
 recipe_id INTEGER NOT NULL,
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (recipe_id) REFERENCES recipes(recipe_id)
);

CREATE TABLE favorites (
 favorite_id INTEGER AUTO_INCREMENT PRIMARY KEY,
 user_id INTEGER NOT NULL,
 recipe_id INTEGER NOT NULL,
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 FOREIGN KEY (user_id) REFERENCES users(user_id),
 FOREIGN KEY (recipe_id) REFERENCES recipes(recipe_id),
 UNIQUE (user_id, recipe_id) -- 同じレシピを重複登録しない
);
