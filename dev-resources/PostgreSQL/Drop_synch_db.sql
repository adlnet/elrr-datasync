-- Navigate to elrr schema
SET search_path = staging;

-- Drop staging tables
DROP TABLE IF EXISTS staging.importdetail cascade;
DROP TABLE IF EXISTS staging."import" cascade;
DROP TABLE IF EXISTS staging.syncrecorddetail cascade;
DROP TABLE IF EXISTS staging.syncrecord cascade;