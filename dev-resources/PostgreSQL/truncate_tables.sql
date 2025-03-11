-- Navigate to staging schema
SET search_path = staging;

-- Truncate tables
TRUNCATE staging.importdetail cascade;
TRUNCATE staging."import" cascade;
TRUNCATE staging.syncrecorddetail cascade;
TRUNCATE staging.syncrecord cascade;
TRUNCATE staging.errors cascade;
TRUNCATE staging.ELRRAuditLog cascade;