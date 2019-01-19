ALTER TABLE website
  ADD COLUMN fetch_every_number_of_hours INTEGER NOT NULL DEFAULT 8760;

ALTER TABLE website_content
  ADD COLUMN time_fetched TIMESTAMP NOT NULL DEFAULT now(),
  ADD COLUMN time_processed TIMESTAMP;
