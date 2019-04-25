#!/bin/bash

cd $(dirname $0)

COMMAND="docker exec -it \
    docker_kafka_dev \
    kafka-topics \
        --list \
        --zookeeper 127.0.0.1:2181";

echo -en "\n$ ";
echo -e $COMMAND;
echo -e "";
$COMMAND
echo -e "";
