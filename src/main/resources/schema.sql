CREATE TABLE recipes (
                         recipe_id INTEGER AUTO_INCREMENT PRIMARY KEY,
                         title VARCHAR(255) NOT NULL,
                         short_description VARCHAR(255),
                         description VARCHAR(1000),
                         image_path VARCHAR(255),
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE ingredients (
                             ingredient_id INTEGER AUTO_INCREMENT PRIMARY KEY,
                             name VARCHAR(255) NOT NULL,
                             quantity VARCHAR(100),
                             recipe_id INTEGER NOT NULL,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (recipe_id) REFERENCES recipes(recipe_id)
);
