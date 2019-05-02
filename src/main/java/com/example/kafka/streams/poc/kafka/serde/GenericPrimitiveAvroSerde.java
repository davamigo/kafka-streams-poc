package com.example.kafka.streams.poc.kafka.serde;

import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Collections;
import java.util.Map;

/**
 * Serializer/deserializer class for primitive types in/to Avro format (Int, Long, String, ...)
 *
 * @param <T> the primitive type:Int, Long, String, ...
 */
public class GenericPrimitiveAvroSerde<T> implements Serde<T> {

    /** Avro Serializer/deserializer object */
    private final Serde<Object> inner;

    /**
     * Default constructor
     */
    public GenericPrimitiveAvroSerde() {
        this.inner = Serdes.serdeFrom(
                new KafkaAvroSerializer(),
                new KafkaAvroDeserializer()
        );
    }

    /**
     * Configure this class, which will configure the underlying serializer and deserializer.
     *
     * @param configs configs in key/value pairs
     * @param isKey   whether is for key or value
     */
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        inner.serializer().configure(configs, isKey);
        inner.deserializer().configure(configs, isKey);
    }

    /**
     * Close this serde class, which will close the underlying serializer and deserializer.
     * This method has to be idempotent because it might be called multiple times.
     */
    @Override
    public void close() {
        inner.serializer().close();
        inner.deserializer().close();
    }

    /**
     * @return the serializer object
     */
    @Override
    @SuppressWarnings("unchecked")
    public Serializer<T> serializer() {
        return (Serializer<T>) inner.serializer();
    }

    /**
     * @return the deserializer object
     */
    @Override
    @SuppressWarnings("unchecked")
    public Deserializer<T> deserializer() {
        return (Deserializer<T>) inner.deserializer();
    }
}
