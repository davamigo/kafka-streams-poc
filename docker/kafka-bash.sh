#!/bin/bash

cd $(dirname $0)

COMMAND="docker exec -it \
    docker_kafka_dev \
    bash";

echo -en "\n$ ";
echo -e $COMMAND;
echo -e "";
$COMMAND
echo -e "";
