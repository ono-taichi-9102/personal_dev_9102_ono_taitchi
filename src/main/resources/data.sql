-- users テーブルにデータを挿入するクエリ（修正版）
INSERT INTO users (name, password)
VALUES('小野太智', '2568723');
INSERT INTO users (name, password)
VALUES('鈴木一郎', 'test123');

-- tasks テーブルにデータを挿入するクエリ（修正版）
INSERT INTO tasks (category_id, user_id, title, closing_date, progress, memo)
VALUES
(2, 1, 'トイレ', '2025-05-28', 0, 'トイレに行く');

-- categories テーブルにデータを挿入するクエリ（修正版）
INSERT INTO categories(name)
VALUES
('仕事');
INSERT INTO categories(name)
VALUES
('個人');
INSERT INTO categories(name)
VALUES
('その他');