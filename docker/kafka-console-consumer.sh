#!/bin/bash

cd $(dirname $0)

ARGS=""
while [[ $# -ge 1 ]]; do
    ARGS="${ARGS} $1"
    shift
done;

COMMAND="docker run -it --rm \
    --net=host \
    confluentinc/cp-schema-registry:3.3.1 \
    kafka-avro-console-consumer \
        --bootstrap-server 127.0.0.1:9092 \
        --property schema.registry.url=http://127.0.0.1:8081 \
        --from-beginning \
        ${ARGS}";

echo -en "\n$ ";
echo -e $COMMAND;
echo -e "";
$COMMAND
echo -e "";
