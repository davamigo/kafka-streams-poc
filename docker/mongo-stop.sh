#!/bin/bash

cd $(dirname $0)

COMMAND="docker stop \
    docker_mongo_dev";

echo -en "\n$ ";
echo -e $COMMAND;
echo -e "";
$COMMAND
echo -e "";
