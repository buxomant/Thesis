CREATE TABLE website_content (
  website_id SERIAL PRIMARY KEY,
  content TEXT,
  FOREIGN KEY (website_id) REFERENCES website (website_id) ON DELETE CASCADE
);

ALTER TABLE website
  DROP COLUMN content;