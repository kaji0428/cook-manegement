-- オムライス
INSERT INTO recipes (title, short_description, description, image_path, created_at)
VALUES (
           'オムライス',
           'ふわとろ卵が絶品!',
           '1. 玉ねぎと鶏肉を一口大に切る。
         2. フライパンにサラダ油を熱し、玉ねぎと鶏肉を炒める。
         3. ご飯を加えてさらに炒め、ケチャップを加えて全体をなじませ、塩こしょうで味を調える。
         4. 別のフライパンで卵2個を溶いて焼き、半熟の状態で火を止める。
         5. 炒めたケチャップライスを皿に盛り、卵をかぶせて形を整える。',
           'images/omurice.jpg',
           CURRENT_TIMESTAMP
       );

INSERT INTO ingredients (name, quantity, recipe_id, created_at) VALUES
                                                                    ('ご飯', '200g', 1, CURRENT_TIMESTAMP),
                                                                    ('卵', '2個', 1, CURRENT_TIMESTAMP),
                                                                    ('鶏肉', '50g', 1, CURRENT_TIMESTAMP),
                                                                    ('玉ねぎ', '1/4個', 1, CURRENT_TIMESTAMP),
                                                                    ('ケチャップ', '大さじ2', 1, CURRENT_TIMESTAMP),
                                                                    ('サラダ油', '適量', 1, CURRENT_TIMESTAMP),
                                                                    ('塩こしょう', '少々', 1, CURRENT_TIMESTAMP);

-- カレーライス
INSERT INTO recipes (title, short_description, description, image_path, created_at)
VALUES (
           'カレーライス',
           'スパイシーで大満足！',
           '1. じゃがいも・にんじん・玉ねぎを食べやすい大きさに切る。
         2. 鍋に油を熱し、切った野菜を炒める。
         3. 水を加えて中火で煮込み、具材がやわらかくなったら火を止める。
         4. カレールウを入れて溶かし、弱火でとろみが出るまで煮る。
         5. ご飯を盛った皿にカレーをかけて完成。',
           'images/curry.jpg',
           CURRENT_TIMESTAMP
       );

INSERT INTO ingredients (name, quantity, recipe_id, created_at) VALUES
                                                                    ('ご飯', '200g', 2, CURRENT_TIMESTAMP),
                                                                    ('カレールウ', '1/2箱', 2, CURRENT_TIMESTAMP),
                                                                    ('じゃがいも', '1個', 2, CURRENT_TIMESTAMP),
                                                                    ('にんじん', '1本', 2, CURRENT_TIMESTAMP),
                                                                    ('玉ねぎ', '1個', 2, CURRENT_TIMESTAMP),
                                                                    ('水', '400ml', 2, CURRENT_TIMESTAMP);

-- ハンバーグ
INSERT INTO recipes (title, short_description, description, image_path, created_at)
VALUES (
           'ハンバーグ',
           'ジューシーな肉の旨味！',
           '1. 玉ねぎをみじん切りにして炒めて冷ます。
         2. ボウルに合いびき肉、炒めた玉ねぎ、パン粉、牛乳、卵、塩こしょうを入れてよく混ぜる。
         3. 空気を抜きながら小判型に成形する。
         4. フライパンで両面に焼き色をつけ、中まで火が通るように弱火で蒸し焼きにする。
         5. お好みでソースをかけて仕上げる。',
           'images/hamburg.jpg',
           CURRENT_TIMESTAMP
       );

INSERT INTO ingredients (name, quantity, recipe_id, created_at) VALUES
                                                                    ('合いびき肉', '200g', 3, CURRENT_TIMESTAMP),
                                                                    ('玉ねぎ', '1/2個', 3, CURRENT_TIMESTAMP),
                                                                    ('パン粉', '大さじ3', 3, CURRENT_TIMESTAMP),
                                                                    ('牛乳', '大さじ2', 3, CURRENT_TIMESTAMP),
                                                                    ('卵', '1個', 3, CURRENT_TIMESTAMP),
                                                                    ('塩こしょう', '少々', 3, CURRENT_TIMESTAMP);

-- ナポリタン
INSERT INTO recipes (title, short_description, description, image_path, created_at)
VALUES (
           'ナポリタン',
           '懐かしい味わい！',
           '1. スパゲッティを表示時間通りに茹でる。
         2. 玉ねぎ、ピーマン、ソーセージを薄切りにする。
         3. フライパンで材料を炒め、茹でたスパゲッティを加える。
         4. ケチャップを加えて炒め合わせ、塩こしょうで味を調える。
         5. 皿に盛りつけて完成。',
           'images/napolitan.jpg',
           CURRENT_TIMESTAMP
       );

INSERT INTO ingredients (name, quantity, recipe_id, created_at) VALUES
                                                                    ('スパゲッティ', '100g', 4, CURRENT_TIMESTAMP),
                                                                    ('玉ねぎ', '1/4個', 4, CURRENT_TIMESTAMP),
                                                                    ('ピーマン', '1個', 4, CURRENT_TIMESTAMP),
                                                                    ('ソーセージ', '2本', 4, CURRENT_TIMESTAMP),
                                                                    ('ケチャップ', '大さじ3', 4, CURRENT_TIMESTAMP),
                                                                    ('塩こしょう', '少々', 4, CURRENT_TIMESTAMP);

-- 親子丼
INSERT INTO recipes (title, short_description, description, image_path, created_at)
VALUES (
           '親子丼',
           'だしの香りが食欲をそそる！',
           '1. 鶏もも肉と玉ねぎを食べやすい大きさに切る。
         2. 鍋にめんつゆと水を入れて加熱し、玉ねぎを煮る。
         3. 鶏肉を加えて火が通るまで煮る。
         4. 溶いた卵を回しかけ、ふんわり火を通す。
         5. ご飯の上に具を乗せて完成。',
           'images/oyakodon.jpg',
           CURRENT_TIMESTAMP
       );

INSERT INTO ingredients (name, quantity, recipe_id, created_at) VALUES
                                                                    ('ご飯', '200g', 5, CURRENT_TIMESTAMP),
                                                                    ('鶏もも肉', '100g', 5, CURRENT_TIMESTAMP),
                                                                    ('玉ねぎ', '1/4個', 5, CURRENT_TIMESTAMP),
                                                                    ('卵', '2個', 5, CURRENT_TIMESTAMP),
                                                                    ('めんつゆ', '50ml', 5, CURRENT_TIMESTAMP),
                                                                    ('水', '100ml', 5, CURRENT_TIMESTAMP);
-- オムライス
INSERT INTO recipes (title, short_description, description, image_path, created_at)
VALUES (
           'ケチャップオムライス',
           'ケチャップたっぷりでトマトの味が広がる！',
           '1. 玉ねぎと鶏肉を一口大に切る。
         2. フライパンにサラダ油を熱し、玉ねぎと鶏肉を炒める。
         3. ご飯を加えてさらに炒め、ケチャップを加えて全体をなじませ、塩こしょうで味を調える。
         4. 別のフライパンで卵2個を溶いて焼き、半熟の状態で火を止める。
         5. 炒めたケチャップライスを皿に盛り、卵をかぶせて形を整える。',
           'images/omurice2.jpg',
           CURRENT_TIMESTAMP
       );

INSERT INTO ingredients (name, quantity, recipe_id, created_at) VALUES
                                                                    ('ご飯', '200g', 6, CURRENT_TIMESTAMP),
                                                                    ('卵', '2個', 6, CURRENT_TIMESTAMP),
                                                                    ('鶏肉', '50g', 6, CURRENT_TIMESTAMP),
                                                                    ('玉ねぎ', '1/4個', 6, CURRENT_TIMESTAMP),
                                                                    ('ケチャップ', '大さじ2', 6, CURRENT_TIMESTAMP),
                                                                    ('サラダ油', '適量', 6, CURRENT_TIMESTAMP),
                                                                    ('塩こしょう', '少々', 6, CURRENT_TIMESTAMP);
-- PW は「track」
INSERT INTO users (username, password) VALUES ('sample', '$2a$08$hBocxV325GV9RKzHR.lHreJr8DsMrkg6vg0kRTbNnBvUFknu670GC');
