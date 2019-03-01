CREATE TABLE text_similarity(
  similarity_id SERIAL PRIMARY KEY,
  first_id INT NOT NULL,
  second_id INT NOT NULL,
  time_frame TEXT NOT NULL,
  first_type TEXT NOT NULL,
  second_type TEXT NOT NULL,
  similarity_coefficient DOUBLE PRECISION
);