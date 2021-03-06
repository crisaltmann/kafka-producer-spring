package br.com.crisaltmann;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

public class MessageSenderString {

	//bin/kafka-topics.sh --create --topic teste-msg --partitions 1 --replication-factor 1 --zookeeper localhost:2181
	
	private static final String TOPIC = "teste-msg";

	private static final String SERVER = "localhost:9092";
	
	private KafkaProducer<String, String> producer;
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		new MessageSenderString().sendMsg();
	}
	
	private void sendMsg() throws InterruptedException, ExecutionException {
		conectKafka();
		
		try {
			long init = System.currentTimeMillis();
			for (int i = 0; i < Integer.MAX_VALUE ; i++)
				sendRandomMsg(i);
			long finish = System.currentTimeMillis();
			System.out.println("Tempo="+(finish-init));
		} finally {
			producer.flush();
			producer.close();
		}
	}

	private void sendRandomMsg(int key) throws InterruptedException, ExecutionException {
		ProducerRecord<String, String> record = new ProducerRecord<String, String>(
				TOPIC, String.valueOf(key), String.valueOf(Math.random()));
		
		producer.send(record);
	}

	private void conectKafka() {
		producer = createProducer(createProperties());
		
	}
	
	private Properties createProperties() {
		Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, SERVER);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "kafka-producer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return props;
	}
	
	private KafkaProducer<String, String> createProducer(Properties prop) {
		return new KafkaProducer<String, String>(prop);
	}
}
