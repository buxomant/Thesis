CREATE TABLE website (
  website_id SERIAL PRIMARY KEY,
  title TEXT NOT NULL,
  url TEXT NOT NULL,
  discovered_on TIMESTAMP NOT NULL,
  last_checked_on TIMESTAMP,
  last_response_code INTEGER
);

CREATE TABLE page (
  page_id SERIAL PRIMARY KEY,
  title TEXT NOT NULL,
  url TEXT NOT NULL,
  discovered_on TIMESTAMP NOT NULL,
  last_checked_on TIMESTAMP,
  last_response_code INTEGER,
  website_id INTEGER,
  CONSTRAINT fk_page_website
  FOREIGN KEY (website_id) REFERENCES website (website_id)
);

CREATE TABLE google_search_term (
  term_id SERIAL PRIMARY KEY,
  term TEXT NOT NULL
);

CREATE TABLE google_search (
  search_id SERIAL PRIMARY KEY,
  term_id INTEGER NOT NULL,
  start_index INTEGER NOT NULL,
  next_start_index INTEGER,
  seen_on TIMESTAMP DEFAULT current_timestamp,
  CONSTRAINT fk_search_term
  FOREIGN KEY (term_id) REFERENCES google_search_term (term_id)
);