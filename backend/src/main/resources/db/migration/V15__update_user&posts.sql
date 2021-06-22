UPDATE persons SET first_name = CONCAT(first_name, '(#' ,persons.id, ')' );
UPDATE posts SET title = CONCAT(title, ' ', REPEAT("0", 3 - LENGTH(posts.id)), posts.id);