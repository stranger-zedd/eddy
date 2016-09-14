#!/bin/sh -e

source /opt/eddy/config.sh

if docker ps -a > /dev/null | grep eddy
then
    docker stop eddy
    docker rm eddy
fi

docker run -e DATA_BUCKET=$DATA_BUCKET -e ZEPHYRUS_BUCKET=$ZEPHYRUS_BUCKET -e EMAIL_HOST=$EMAIL_HOST -e EMAIL_PASS=$EMAIL_PASS -e EMAIL_USER=$EMAIL_USER --name eddy -d eddy:latest
