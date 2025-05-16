-- 各種テーブル削除
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS tasks;

-- users テーブルを作成するクエリ
CREATE TABLE users (
id SERIAL PRIMARY KEY,
name VARCHAR(20) NOT NULL UNIQUE,
password VARCHAR(50) NOT NULL
);

-- tasks テーブルを作成するクエリ
CREATE TABLE tasks (
id SERIAL PRIMARY KEY,
category_id INTEGER NOT NULL,
user_id INTEGER NOT NULL,
title VARCHAR(255) NOT NULL,
closing_date DATE NOT NULL,
progress INTEGER NOT NULL,
memo TEXT NOT NULL
);

--categoriesテーブルを作成するクエリ
CREATE TABLE categories(
id SERIAL PRIMARY KEY,
name VARCHAR(20) NOT NULL
);