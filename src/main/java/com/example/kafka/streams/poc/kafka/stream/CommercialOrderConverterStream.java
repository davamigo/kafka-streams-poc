package com.example.kafka.streams.poc.kafka.stream;

import com.example.kafka.streams.poc.kafka.serde.GenericPrimitiveAvroSerde;
import com.example.kafka.streams.poc.schemas.order.CommercialOrder;
import com.example.kafka.streams.poc.schemas.order.CommercialOrderConverted;
import com.example.kafka.streams.poc.schemas.member.Member;
import com.example.kafka.streams.poc.schemas.order.CommercialOrderLine;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Kafka streams for converting the new commercial orders
 */
@Component
public class CommercialOrderConverterStream extends BaseStream {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(CommercialOrderConverterStream.class);

    /** The name of the new members Kafka topic (input KTable) */
    private final String newMembersTopic;

    /** The name of the new commercial orders Kafka topic (input KStream) */
    private final String newCommercialOrdersTopic;

    /** The name of the converted commercial orders Kafka topic (output KStream) */
    private final String convertedCommercialOrdersTopic;

    /**
     * Autowired constructor
     *
     * @param schemaRegistryUrl              the URL of the schema registry
     * @param newMembersTopic                the name of the new members Kafka topic (input KTable)
     * @param newCommercialOrdersTopic       the name of the new commercial orders Kafka topic (input KStream)
     * @param convertedCommercialOrdersTopic the name of the converted commercial orders Kafka topic (output KStream)
     */
    @Autowired
    public CommercialOrderConverterStream(
            @Value("${spring.kafka.schema-registry-url}") String schemaRegistryUrl,
            @Value("${spring.kafka.topics.members-new}") String newMembersTopic,
            @Value("${spring.kafka.topics.commercial-orders-new}") String newCommercialOrdersTopic,
            @Value("${spring.kafka.topics.commercial-orders-converted}") String convertedCommercialOrdersTopic
    ) {
        super(schemaRegistryUrl);
        this.newMembersTopic = newMembersTopic;
        this.newCommercialOrdersTopic = newCommercialOrdersTopic;
        this.convertedCommercialOrdersTopic = convertedCommercialOrdersTopic;
    }

    /**
     * Builds the topology of the Kafka Streams
     *
     * @param builder the streams builder
     * @return the result KStream
     */
    @Bean("commercialOrderConverterStreamTopology")
    public KStream<String, CommercialOrderConverted> startProcessing(
            @Qualifier("commercialOrderConverterStreamBuilderFactoryBean") StreamsBuilder builder
    ) {
        final Serde<String> stringKeyAvroSerde = new GenericPrimitiveAvroSerde<>();
        stringKeyAvroSerde.configure(serdeConfig, true);

        final Serde<Member> memberValueAvroSerde = new SpecificAvroSerde<>();
        memberValueAvroSerde.configure(serdeConfig, false);

        final Serde<CommercialOrder> commercialOrderValueAvroSerde = new SpecificAvroSerde<>();
        commercialOrderValueAvroSerde.configure(serdeConfig, false);

        final Serde<CommercialOrderConverted> commercialOrderConvertedValueAvroSerde = new SpecificAvroSerde<>();
        commercialOrderConvertedValueAvroSerde.configure(serdeConfig, false);

        GlobalKTable<String, Member> membersGlobalTable = builder.globalTable(
                newMembersTopic,
                Consumed.with(stringKeyAvroSerde, memberValueAvroSerde)
        );

        KStream<String, CommercialOrder> commercialOrdersStream = builder.stream(
                newCommercialOrdersTopic,
                Consumed.with(stringKeyAvroSerde, commercialOrderValueAvroSerde)
        );

        KStream<String, CommercialOrderConverted> commercialOrdersConvertedStream = commercialOrdersStream
                .join(
                        membersGlobalTable,
                        (String uuid, CommercialOrder commercialOrder) -> commercialOrder.getMemberUuid(),
                        (CommercialOrder commercialOrder, Member member) -> {
                            LOGGER.info(">>> Stream - Commercial order uuid={} joined with member uuid={}.", commercialOrder.getUuid(), commercialOrder.getMemberUuid());

                        double amount = commercialOrder.getLines().stream().mapToDouble(line -> line.getPrice() * line.getQuantity()).sum();

                        int quantity = commercialOrder.getLines().stream().mapToInt(CommercialOrderLine::getQuantity).sum();

                            return CommercialOrderConverted
                                    .newBuilder()
                                    .setUuid(commercialOrder.getUuid())
                                    .setDatetime(commercialOrder.getDatetime())
                                    .setMemberUuid(member.getUuid())
                                    .setMemberFirstName(member.getFirstName())
                                    .setMemberLastName(member.getLastName())
                                    .setShippingCountry(commercialOrder.getShippingAddress().getCountry())
                                    .setShippingCity(commercialOrder.getShippingAddress().getCity())
                                    .setShippingZipCode(commercialOrder.getShippingAddress().getZipCode())
                                    .setTotalAmount((float) amount)
                                    .setTotalQuantity(quantity)
                                    .build();
                        }
                );

        commercialOrdersConvertedStream.to(
                convertedCommercialOrdersTopic,
                Produced.with(stringKeyAvroSerde, commercialOrderConvertedValueAvroSerde)
        );

        return commercialOrdersConvertedStream;
    }
}
