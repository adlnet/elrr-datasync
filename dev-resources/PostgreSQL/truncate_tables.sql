-- Navigate to staging schema
SET search_path = staging;

-- Truncate tables
TRUNCATE staging."import" cascade;
TRUNCATE staging.ELRRAuditLog cascade;