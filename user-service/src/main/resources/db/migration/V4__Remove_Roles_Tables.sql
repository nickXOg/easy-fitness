-- Remove roles and user_roles tables and their relationships
-- This script drops the roles and user_roles tables that are no longer needed

-- Drop the user_roles junction table first (due to foreign key constraints)
DROP TABLE IF EXISTS user_roles;

-- Drop the roles table
DROP TABLE IF EXISTS roles;