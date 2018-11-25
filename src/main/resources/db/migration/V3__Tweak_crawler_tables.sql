ALTER TABLE website
  ADD COLUMN content TEXT,
  ADD COLUMN error TEXT,
  ADD COLUMN redirects_to_external BOOLEAN DEFAULT FALSE;

ALTER TABLE page
  ADD COLUMN parent_page_id INT,
  ADD COLUMN content TEXT,
  ADD COLUMN error TEXT,
  ADD CONSTRAINT fk_parent_page_id
    FOREIGN KEY (parent_page_id) REFERENCES page (page_id);

CREATE TABLE links_to (
  link_id SERIAL PRIMARY KEY,
  website_id_from INT NOT NULL,
  website_id_to INT NOT NULL,
  CONSTRAINT fk_website_id_from
    FOREIGN KEY (website_id_from) REFERENCES website (website_id),
  CONSTRAINT fk_website_id_to
    FOREIGN KEY (website_id_to) REFERENCES website (website_id)
);