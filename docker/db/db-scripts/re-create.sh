#!/bin/sh

# re-create.sh
psql -U postgres -d vaka_daily_db -f /sql-scripts/sql/drop.sql
psql -U postgres -d vaka_daily_db -f /sql-scripts/sql/create.sql
psql -U postgres -d vaka_daily_db -f /sql-scripts/sql/insert.sql