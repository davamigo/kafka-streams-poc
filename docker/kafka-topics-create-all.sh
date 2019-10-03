#!/bin/bash

cd $(dirname $0)

REGULAR_30DAYS_TOPICS="\
    t.commercial-order-lines.split \
    ";

COMPACTED_30DAYS_TOPICS="\
    t.purchase-order-lines.aggregated \
    t.warehouse-order-lines.generated \
    t.warehouse-order-lines.matched \
    t.warehouse-order-lines.unmatched \
    t.warehouse-order-lines.recovered \
    t.warehouse-order-lines.failed \
    t.warehouse-order-lines.new \
    t.product-legacy-ids.cache \
    ";

COMPACTED_FOREVER_TOPICS="\
    t.members.new \
    t.products.new \
    t.commercial-orders.new \
    t.commercial-orders.converted \
    t.purchase-orders.generated \
    t.warehouse-orders.new \
    ";


###############################################################################
# REGULAR 30 DAYS TOPIC CREATION
###############################################################################

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


###############################################################################
# COMPACTED 30 DAYS TOPIC CREATION
###############################################################################

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


###############################################################################
# COMPACTED LAST FOREVER TOPIC CREATION
###############################################################################

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
