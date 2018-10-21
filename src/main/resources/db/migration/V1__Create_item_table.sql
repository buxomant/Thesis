CREATE TABLE item (
  item_id SERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  url TEXT NOT NULL
);

CREATE TABLE item_price (
  item_price_id SERIAL PRIMARY KEY,
  item_id INTEGER NOT NULL,
  price NUMERIC NOT NULL,
  time_checked TIMESTAMP NOT NULL DEFAULT current_timestamp,
  CONSTRAINT fk_item_price_item_id
  FOREIGN KEY (item_id) REFERENCES item (item_id)
);