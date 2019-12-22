package service.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import service.api.UpperCaseService;
import service.model.JsonPOJODeserializer;
import service.model.JsonPOJOSerializer;
import service.model.UpperResponseMsg;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class KafkaProcessor implements Runnable {

    private KafkaStreams kafkaStreams;

    public KafkaProcessor(UpperCaseService service) {
        StreamsConfig streamsConfig = new StreamsConfig(getProperties());
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, String> incomingMsgStream = streamsBuilder.stream("IncomingMessages", Consumed.with(Serdes.String(), Serdes.String()));
        KStream<String, UpperResponseMsg> responseMsgStream = incomingMsgStream.mapValues(msg -> new UpperResponseMsg(Instant.now(), msg, service.toUpper(msg)));
        responseMsgStream.to("ResponseMessages", Produced.with(Serdes.String(), getSerde()));
        kafkaStreams = new KafkaStreams(streamsBuilder.build(), streamsConfig);
    }

    private Serde<UpperResponseMsg> getSerde() {
        Map<String, Object> serdeProps = new HashMap<>();
        serdeProps.put("JsonPOJOClass", UpperResponseMsg.class);
        Serializer<UpperResponseMsg> serializer = new JsonPOJOSerializer<>();
        serializer.configure(serdeProps, false);
        Deserializer<UpperResponseMsg> deserializer = new JsonPOJODeserializer<>();
        deserializer.configure(serdeProps, false);
        return Serdes.serdeFrom(serializer, deserializer);
    }

    @Override
    public void run() {
        kafkaStreams.start();
    }

    public void close() {
        kafkaStreams.close();
    }

    private Properties getProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.CLIENT_ID_CONFIG, "Hello-Service-Client");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "micro-services");
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "Hello-App");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-server.service.consul:9092");
        props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, 1);
        props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class);
        return props;
    }
}
