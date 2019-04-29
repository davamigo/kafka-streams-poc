#!/bin/bash

cd $(dirname $0)

COMMAND="docker run --rm -d \
    -p 27017:27017 \
    -e MONGODB_ROOT_PASSWORD=temporal \
    --name docker_mongo_dev \
    bitnami/mongodb:3.7";

echo -en "\n$ ";
echo -e $COMMAND;
echo -e "";
$COMMAND
echo -e "";
