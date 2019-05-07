#!/bin/bash

cd $(dirname $0)

COMPACTED_TOPICS="\
    t.members.new \
    t.products.new \
    t.purchase-order-lines.aggregated \
    ";

for TOPIC in ${COMPACTED_TOPICS}; do
    COMMAND="docker exec -it \
        docker_kafka_dev \
        kafka-topics \
            --create \
            --zookeeper 127.0.0.1:2181 \
            --partitions 3 \
            --replication-factor 1 \
            --config cleanup.policy=compact \
            --config retention.bytes=-1 \
            --config retention.ms=-1 \
            --topic ${TOPIC}";

    echo -en "\n$ ";
    echo -e $COMMAND;
    echo -e "";
    $COMMAND
    echo -e "";
done;

REGULAR_TOPICS="\
    t.commercial-orders.new \
    t.commercial-orders.converted \
    t.commercial-order-lines.split \
    ";

for TOPIC in ${REGULAR_TOPICS}; do
    COMMAND="docker exec -it \
        docker_kafka_dev \
        kafka-topics \
            --create \
            --zookeeper 127.0.0.1:2181 \
            --partitions 3 \
            --replication-factor 1 \
            --config cleanup.policy=delete \
            --config retention.bytes=-1 \
            --config retention.ms=2592000000 \
            --topic ${TOPIC}";

    echo -en "\n$ ";
    echo -e $COMMAND;
    echo -e "";
    $COMMAND
    echo -e "";
done;
