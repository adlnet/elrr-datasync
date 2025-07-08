-- Navigate to datasync_schema schema
SET search_path = datasync_schema;

-- Truncate tables
TRUNCATE datasync_schema."import" cascade;
TRUNCATE datasync_schema.ELRRAuditLog cascade;