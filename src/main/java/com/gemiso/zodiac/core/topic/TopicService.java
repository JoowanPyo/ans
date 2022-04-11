package com.gemiso.zodiac.core.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
@RequiredArgsConstructor
public class TopicService {

    @Value("${topic.routing-key:routiongKey}")
    private String routingKey;

    private static final String EXCHANGE_WEB_NAME = "ans.topic";
    private static final String EXCHANGE_INTERFACE_NAME = "ans.interface";

    public void topicWeb(String msg){

        Connection connection = null;
        Channel channel = null;

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("172.20.10.151");
        factory.setPort(5672);
        //factory.setVirtualHost("/");
        factory.setUsername("rabbitmq");
        factory.setPassword("rabbitmq");

        log.info("ans.topic : EXCHANGE : "+EXCHANGE_WEB_NAME + " HOST : "+ factory.getHost() +" PORT : "+ factory.getPort() +" USER : "+factory.getUsername());
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();

            //channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            channel.queueDeclare(EXCHANGE_WEB_NAME, false, false, false, null);

            //String routingKey = "ans";
            //String message = "test ans";

            channel.basicPublish(EXCHANGE_WEB_NAME, routingKey, null, msg.getBytes(StandardCharsets.UTF_8));

            log.info("ans.topic Sent "+"EXCHANGE : " +EXCHANGE_WEB_NAME+" ROUTINGKEY : "+ routingKey +" MESSAGE : "+ msg );
        }
        catch (TimeoutException e) {
            e.printStackTrace();
            log.error("ans.topic error : " +e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("ans.topic error : " +e.getMessage());
        }
    }

    public void topicInterface(String msg){

        Connection connection = null;
        Channel channel = null;

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("172.20.10.151");
        factory.setPort(5672);
        //factory.setVirtualHost("/");
        factory.setUsername("rabbitmq");
        factory.setPassword("rabbitmq");

        log.info("ans.interface : EXCHANGE : "+EXCHANGE_INTERFACE_NAME + " HOST : "+ factory.getHost() +" PORT : "+ factory.getPort() +" USER : "+factory.getUsername());

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();

            //channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            channel.queueDeclare(EXCHANGE_INTERFACE_NAME, false, false, false, null);

            //String routingKey = "ans";
            //String message = "test ans";

            channel.basicPublish(EXCHANGE_INTERFACE_NAME, routingKey, null, msg.getBytes(StandardCharsets.UTF_8));

            log.info("ans.interface Sent "+"EXCHANGE : " +EXCHANGE_INTERFACE_NAME+" ROUTINGKEY : "+ routingKey +" MESSAGE : "+ msg );
        }
        catch (TimeoutException e) {
            e.printStackTrace();
            log.error("ans.interface error : " +e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("ans.interface error : " +e.getMessage());
        }
    }
}
