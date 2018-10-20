CREATE TABLE item (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  language_name TEXT NOT NULL,
  url TEXT NOT NULL
);

CREATE TABLE item_price_history (
  id SERIAL PRIMARY KEY,
  item_id INTEGER NOT NULL,
  price NUMERIC NOT NULL,
  CONSTRAINT fk_item_price_history_item_id
  FOREIGN KEY (item_id) REFERENCES item (id)
);