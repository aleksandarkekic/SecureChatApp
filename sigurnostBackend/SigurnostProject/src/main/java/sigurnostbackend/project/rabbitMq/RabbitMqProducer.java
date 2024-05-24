package sigurnostbackend.project.rabbitMq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import sigurnostbackend.project.MessagePartition.MessagePart;

@Service
public class RabbitMqProducer {
    @Value("${rabbitmq.exchange.name1}")
    private String exchange1;

    @Value("${rabbitmq.exchange.name2}")
    private String exchange2;

    @Value("${rabbitmq.exchange.name3}")
    private String exchange3;

    @Value("${rabbitmq.exchange.name4}")
    private String exchange4;

    private Queue queue1;
    private Queue queue2;
    private Queue queue3;
    private Queue queue4;



    private RabbitTemplate rabbitTemplate1;

    private RabbitTemplate rabbitTemplate2;

    private RabbitTemplate rabbitTemplate3;

    private RabbitTemplate rabbitTemplate4;
    @Value("${rabbitmq.routingkey.name1}")
    private String key1;

    @Value("${rabbitmq.routingkey.name2}")
    private String key2;

    @Value("${rabbitmq.routingkey.name3}")
    private String key3;

    @Value("${rabbitmq.routingkey.name4}")
    private String key4;

    @Autowired
    public RabbitMqProducer(@Qualifier("server1") Queue queue1, @Qualifier("server2") Queue queue2, @Qualifier("server3") Queue queue3, @Qualifier("server4") Queue queue4, @Qualifier("server1") RabbitTemplate rabbitTemplate1, @Qualifier("server2") RabbitTemplate rabbitTemplate2, @Qualifier("server3") RabbitTemplate rabbitTemplate3, @Qualifier("server4") RabbitTemplate rabbitTemplate4) {
        this.queue1 = queue1;
        this.queue2 = queue2;
        this.queue3 = queue3;
        this.queue4 = queue4;

        this.rabbitTemplate1 = rabbitTemplate1;
        this.rabbitTemplate2 = rabbitTemplate2;
        this.rabbitTemplate3 = rabbitTemplate3;
        this.rabbitTemplate4 = rabbitTemplate4;
    }

    public void sendMessage1(MessagePart messagePart){
        try {
            rabbitTemplate1.convertAndSend(exchange1,key1,messagePart);
        }catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }

    }

    public void sendMessage2(MessagePart messagePart){
        try {
            rabbitTemplate2.convertAndSend(exchange2, key2, messagePart);
        }catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public void sendMessage3(MessagePart messagePart){
        rabbitTemplate3.convertAndSend(exchange3,key3,messagePart);
    }

    public void sendMessage4(MessagePart messagePart){
        rabbitTemplate4.convertAndSend(exchange4,key4,messagePart);
    }
}
