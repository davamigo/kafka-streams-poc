#!/bin/bash

cd $(dirname $0)

ARGS=""
while [[ $# -ge 1 ]]; do
    ARGS="${ARGS} $1"
    shift
done;

COMMAND="docker-compose ${ARGS}";

echo -en "\n$ ";
echo -e $COMMAND;
echo -e "";
$COMMAND
echo -e "";
