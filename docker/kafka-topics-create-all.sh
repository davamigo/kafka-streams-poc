#!/bin/bash

cd $(dirname $0)

REGULAR_30DAYS_TOPICS="\
    t.commercial-orders.new \
    t.commercial-orders.converted \
    t.commercial-order-lines.split \
    t.warehouse-order-lines.matched \
    t.warehouse-order-lines.unmatched \
    t.warehouse-order-lines.recovered \
    t.warehouse-order-lines.failed
     t.warehouse-order-lines.new \\
    ";

for TOPIC in ${REGULAR_30DAYS_TOPICS}; do
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

COMPACTED_30DAYS_TOPICS="\
    t.purchase-order-lines.aggregated \
    t.purchase-orders.generated \
    t.product-legacy-ids.cache \
    ";

for TOPIC in ${COMPACTED_30DAYS_TOPICS}; do
    COMMAND="docker exec -it \
        docker_kafka_dev \
        kafka-topics \
            --create \
            --zookeeper 127.0.0.1:2181 \
            --partitions 3 \
            --replication-factor 1 \
            --config cleanup.policy=compact \
            --config retention.bytes=-1 \
            --config retention.ms=2592000000 \
            --topic ${TOPIC}";

    echo -en "\n$ ";
    echo -e $COMMAND;
    echo -e "";
    $COMMAND
    echo -e "";
done;

COMPACTED_FOREVER_TOPICS="\
    t.members.new \
    t.products.new \
    ";

for TOPIC in ${COMPACTED_FOREVER_TOPICS}; do
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
