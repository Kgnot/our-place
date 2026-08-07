FROM postgres:16-alpine

COPY dbv1.sql /docker-entrypoint-initdb.d/01-dbv1.sql
COPY datos_iniciales.sql /docker-entrypoint-initdb.d/02-datos_iniciales.sql