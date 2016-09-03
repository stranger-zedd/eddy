FROM java:8-alpine

RUN apk update
RUN apk add postfix postfix-pcre ca-certificates

COPY target/eddy-current.jar /opt/eddy/eddy.jar
COPY script/entrypoint.sh /opt/eddy/eddy

WORKDIR /opt/eddy

ENTRYPOINT ./eddy
