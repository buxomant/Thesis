CREATE TABLE website_text_similarity(
  similarity_id SERIAL PRIMARY KEY,
  first_website_id INT NOT NULL,
  second_website_id INT NOT NULL,
  time_frame TEXT NOT NULL,
  similarity_coefficient DOUBLE PRECISION
);