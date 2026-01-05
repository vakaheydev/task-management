FROM postgres

RUN mkdir /sql-scripts

WORKDIR /sql-scripts

COPY db-scripts/sql /docker-entrypoint-initdb.d/

RUN chmod +x /docker-entrypoint-initdb.d/*

COPY db-scripts .

RUN chmod +x /sql-scripts/re-create.sh