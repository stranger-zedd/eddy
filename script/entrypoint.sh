#!/bin/sh

if ! $(postfix status)
then
    postfix start
fi

java -jar "${JAR_PATH-.}/eddy.jar"
