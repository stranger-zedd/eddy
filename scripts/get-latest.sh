#!/bin/sh -e
source /opt/eddy/config.sh

mkdir -p /tmp/eddy/install
aws s3 cp "s3://${EDDY_BUCKET}/${EDDY_IMAGE_FILE}" /tmp/eddy-image.tar.gz
gunzip -c /tmp/eddy-image.tar.gz > /tmp/eddy-image.tar
docker load < /tmp/eddy-image.tar
