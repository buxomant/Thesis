ALTER TABLE website
  ADD COLUMN last_processed_on TIMESTAMP;

ALTER TABLE page
  ADD COLUMN last_processed_on TIMESTAMP;