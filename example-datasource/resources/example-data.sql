
CREATE TABLE basic_types_table (
  bigserial_pkey BIGSERIAL PRIMARY KEY,
  timestamp_now TIMESTAMP WITH TIME ZONE DEFAULT now(),
  some_int INTEGER NOT NULL,
  some_varchar VARCHAR(256) NOT NULL,
  optional_text TEXT NULL,
  uuid UUID NOT NULL,
  some_statement BOOLEAN NOT NULL default FALSE,
  jsonb JSONB);

comment on column basic_types_table.uuid is 'Commenting UUID!';
CREATE INDEX ON basic_types_table (uuid);

CREATE VIEW latest_basic_types_table AS 
SELECT * FROM basic_types_table ORDER BY timestamp_now DESC LIMIT 1;

CREATE TABLE referencing_table (
  id BIGSERIAL PRIMARY KEY,
  bigserial_refkey bigint references basic_types_table(bigserial_pkey),
  timestamp_now TIMESTAMP WITH TIME ZONE DEFAULT now());





