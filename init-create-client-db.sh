#!/bin/bash
set -e

# Create a database with the name of POSTGRES_DB + '_client'
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE "${POSTGRES_DB}_client";
EOSQL
