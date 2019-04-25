#!/bin/bash

cd $(dirname $0)

COMMAND="docker exec -it \
    docker_kafka_dev \
    kafka-topics \
        --create \
        --zookeeper 127.0.0.1:2181 |
        --partitions 3 \
        --replication-factor 1 \
        --config cleanup.policy=compact \
        --config retention.bytes=-1 \
        --config retention.ms=-1 \
        --topic t.commercial-orders.new";

echo -en "\n$ ";
echo -e $COMMAND;
echo -e "";
$COMMAND
echo -e "";


COMMAND="docker exec -it \
    docker_kafka_dev \
    kafka-topics \
        --create \
        --zookeeper 127.0.0.1:2181 |
        --partitions 3 \
        --replication-factor 1 \
        --config cleanup.policy=compact \
        --config retention.bytes=-1 \
        --config retention.ms=-1 \
        --topic t.members.new";

echo -en "\n$ ";
echo -e $COMMAND;
echo -e "";
$COMMAND
echo -e "";


COMMAND="docker exec -it \
    docker_kafka_dev \
    kafka-topics \
        --create \
        --zookeeper 127.0.0.1:2181 |
        --partitions 3 \
        --replication-factor 1 \
        --config cleanup.policy=compact \
        --config retention.bytes=-1 \
        --config retention.ms=-1 \
        --topic t.products.new";

echo -en "\n$ ";
echo -e $COMMAND;
echo -e "";
$COMMAND
echo -e "";
