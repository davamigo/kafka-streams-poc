#!/bin/bash

cd $(dirname $0)

COMMAND="docker run --rm -d \
    -p 2181:2181 -p 3030:3030 \
    -p 8081:8081 -p 8082:8082 \
    -p 8083:8083 -p 9092:9092 \
    -e ADV_HOST=127.0.0.1 \
    -e RUNTESTS=0 \
    -e SAMPLEDATA=0 \
    -e FORWARDLOGS=0 \
    -v $PWD/../scripts:/container/scripts \
    -v $PWD/../src/main/avro:/container/avro \
    --name docker_kafka_dev \
    landoop/fast-data-dev:2.0";

echo -en "\n$ ";
echo -e $COMMAND;
echo -e "";
$COMMAND
echo -e "";
