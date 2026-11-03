FROM postgres:16-alpine

ENV POSTGRES_DB=safepay

COPY init/ /docker-entrypoint-initdb.d/
