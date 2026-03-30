#!/bin/sh

# re-create.sh
psql -U postgres -d vaka_daily_db -f /sql/drop.sql
psql -U postgres -d vaka_daily_db -f /sql/create.sql
psql -U postgres -d vaka_daily_db -f /sql/insert.sql