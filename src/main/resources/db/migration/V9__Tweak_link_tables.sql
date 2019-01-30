ALTER TABLE website_content
  DROP CONSTRAINT website_content_pkey,
  ADD content_id SERIAL PRIMARY KEY;

ALTER TABLE links_to
  ADD COLUMN content_id INT,
  ADD CONSTRAINT fk_website_link_content_id FOREIGN KEY (content_id) REFERENCES website_content (content_id);

ALTER TABLE links_to
  RENAME TO website_to_website;

CREATE TABLE page_to_page(
  link_id SERIAL PRIMARY KEY,
  page_id_from INT NOT NULL,
  page_id_to INT NOT NULL,
  content_id INT NOT NULL,
  CONSTRAINT fk_page_link_content_id FOREIGN KEY (content_id) REFERENCES website_content (content_id),
  CONSTRAINT fk_page_id_from FOREIGN KEY (page_id_from) REFERENCES page (page_id),
  CONSTRAINT fk_page_id_to FOREIGN KEY (page_id_to) REFERENCES page (page_id)
);