FROM postgres

COPY ./rest-api/src/main/resources/db /sql

COPY ./docker/db/db-scripts/re-create.sh /re-create.sh
RUN chmod +x /re-create.sh
