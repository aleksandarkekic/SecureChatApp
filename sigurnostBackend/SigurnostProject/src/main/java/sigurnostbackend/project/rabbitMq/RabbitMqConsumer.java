package sigurnostbackend.project.rabbitMq;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import sigurnostbackend.project.MessagePartition.MessagePart;

import java.io.DataInput;
import java.io.IOException;

@Service
public class RabbitMqConsumer {

private static ObjectMapper objectMapper;
private static final Logger LOGGER= LoggerFactory.getLogger(RabbitMqConsumer.class);

public static MessagePart messageParts[]=new MessagePart[4];

@Autowired
public RabbitMqConsumer(ObjectMapper objectMapper){
    this.objectMapper=objectMapper;
}


    @RabbitListener(queues = {"${rabbitmq.queue.name1}"}, containerFactory = "factory1")
    public static void consumeMessage1(MessagePart messagePart){
       LOGGER.info(String.format("Received message from server1-> %s ",messagePart.toString()));
        try {
            //MessagePart messagePart1 = objectMapper.readValue(messagePart.getText(), MessagePart.class);
            messageParts[0]=messagePart;
            if(messagePart!=null){
                System.out.println("////////////////////"+messagePart.toString());
            }else{
                System.out.println("NULLLLLLLLLLLL JEEEEEEEEEEEEE");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
//       MessagePart ms=new MessagePart();
//       ms.setSenderId(messagePart.getSenderId());
//       ms.setText(messagePart.getText());
//       ms.setReceiverId(messagePart.getReceiverId());
//       ms.setNumOfPart(messagePart.getNumOfPart());
//       messageParts[1]=ms;
    }

    @RabbitListener(queues = {"${rabbitmq.queue.name2}"},  containerFactory = "factory2")
    public static void consumeMessage2(MessagePart messagePart){
        LOGGER.info(String.format("Received message from server2 -> %s ",messagePart.toString()));
        try {
            //MessagePart messagePart1 = objectMapper.readValue(messagePart.getText(), MessagePart.class);
            messageParts[1]=messagePart;
            if(messagePart!=null){
                System.out.println("////////////////////"+messageParts[1].toString());
            }else{
                System.out.println("NULLLLLLLLLLLL JEEEEEEEEEEEEE");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = {"${rabbitmq.queue.name3}"},  containerFactory = "factory3")
    public static void consumeMessage3(MessagePart messagePart){
        LOGGER.info(String.format("Received message from server3 -> %s ",messagePart.toString()));
        try {

            messageParts[2]=messagePart;
            if(messagePart!=null){
                System.out.println("////////////////////"+messageParts[2].toString());
            }else{
                System.out.println("NULLLLLLLLLLLL JEEEEEEEEEEEEE");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = {"${rabbitmq.queue.name4}"},  containerFactory = "factory4")
    public static void consumeMessage4(MessagePart messagePart){
        LOGGER.info(String.format("Received message from server4-> %s ",messagePart.toString()));
        try {
            //MessagePart messagePart1 = objectMapper.readValue(messagePart.getText(), MessagePart.class);
            messageParts[3]=messagePart;
            if(messagePart!=null){
                System.out.println("////////////////////"+messageParts[3].toString());
            }else{
                System.out.println("NULLLLLLLLLLLL JEEEEEEEEEEEEE");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
