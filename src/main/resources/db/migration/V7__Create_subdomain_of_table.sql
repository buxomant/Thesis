CREATE TABLE subdomain_of (
  subdomain_relation_id SERIAL PRIMARY KEY,
  website_id_parent INT NOT NULL,
  website_id_child INT NOT NULL,
  CONSTRAINT fk_website_id_parent
    FOREIGN KEY (website_id_parent) REFERENCES website (website_id),
  CONSTRAINT fk_website_id_child
    FOREIGN KEY (website_id_child) REFERENCES website (website_id)
);

