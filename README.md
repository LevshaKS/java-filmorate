# Filmorate
Это проект кинотеки, в который можно добавлять комментарии, оценивать фильмы находить самые популярные а так добавлять
в друзья пользователей кинотеки.

# Схема БД
<img alt="Схема БД" src="filmorate.png">

# Примеры запросов

### Для пользователей

* Создание пользователя
```SQL
INSERT INTO users(email,
            login,
            name,
            birthday)
VALUES (?,?,?,?);
```

* Обновление пользователя
```SQL
UPDATE users
 SET email=?,
     login=?, 
     name=?, 
     birthday=? 
 WHERE id=?;
```
* Получение пользоывтеля по ID
```SQL
SELECT * FROM users WHERE id=?;
```
* Получение всех пользователей
```SQL
SELECT * FROM users;
```
* Удаление пользователя
 ```SQL
DELETE FROM users WHERE id=?;
```
* Добавление в список друзей
 ```SQL
INSERT INTO friends (user_id, to_user_id) VALUES (?,?);
```
* Получение списка друзей
 ```SQL
"SELECT to_user_id FROM friends WHERE user_id=?;
```
* Обновление списка друзей
 ```SQL
DELETE FROM users WHERE id=?;
```
### Для фильмов

* Добавление фильма
 ```SQL
INSERT INTO films(name,
            description, 
            release_date, 
            duration, 
            mpa_rating_id) 
 VALUES(?,?,?,?,?);
```
* Обновление фильма
 ```SQL
UPDATE films SET name=?, 
                description=?, 
                release_date=?, 
                duration=?, 
                mpa_rating_id=? 
WHERE id=?;
```
* Удаление фильма
 ```SQL
DELETE FROM films WHERE id=?;
```
* Получение фильма по ID
 ```SQL
SELECT * FROM films WHERE id=?;
```
* Получение всех фильмов
 ```SQL
SELECT * FROM films;
```
* Получение списка лайков фильма
 ```SQL
SELECT user_id FROM film_likes WHERE film_id=?;
```
* Добавление лайка фильма
 ```SQL
INSERT INTO film_likes(film_id, user_id) VALUES (?,?);
```
* Удвление лайка фильма
 ```SQL
DELETE FROM film_likes WHERE film_id=? AND user_id=?;
```
* Получение списка жанров фильма
 ```SQL
SELECT g.genre_id AS id, gs.name AS name 
    FROM genre AS g LEFT OUTER JOIN genres AS gs ON g.genre_id = gs.id 
    WHERE g.film_id =? ORDER BY id";
```
