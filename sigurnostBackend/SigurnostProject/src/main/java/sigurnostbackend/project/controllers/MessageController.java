package sigurnostbackend.project.controllers;

import sigurnostbackend.project.MessagePartition.MessageDivision;
import sigurnostbackend.project.MessagePartition.MessagePart;
import sigurnostbackend.project.crypto.Certificate;
import sigurnostbackend.project.crypto.Dgst;
import sigurnostbackend.project.crypto.Encryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sigurnostbackend.project.SigurnostProjectApplication;
import sigurnostbackend.project.crypto.ImgStegenography;
import sigurnostbackend.project.exceptions.DigitalSignatureException;
import sigurnostbackend.project.models.dto.MessageDecrypt;
import sigurnostbackend.project.models.requests.MessageRequest;
import sigurnostbackend.project.models.requests.MessageRequestFull;
import sigurnostbackend.project.rabbitMq.RabbitMqConsumer;
import sigurnostbackend.project.rabbitMq.RabbitMqProducer;
import sigurnostbackend.project.services.MessageService;
import sigurnostbackend.project.services.UserService;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/messages")
public class MessageController {
    private MessageService messageService;
    private UserService userService;

    private RabbitMqProducer producer;
    private RabbitMqConsumer consumer;

    public static byte[] digitalSignature=null;
public static int length=0;
    @Autowired
    public MessageController(MessageService messageService,UserService userService,RabbitMqConsumer consumer, RabbitMqProducer producer){
        this.messageService=messageService;
        this.userService=userService;
       this.producer=producer;
        this.consumer=consumer;
    }

    //na frontu cu poslati receiverID
    @PostMapping("/insert")
    public MessageRequestFull insert(@RequestBody MessageRequest messageRequest) throws Exception {
        MessageRequestFull ms=new MessageRequestFull();
        ms.setSenderId(userService.getCurrentUser().getId());
        ms.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        String text=messageRequest.getText();
        length=text.length();
        ms.setChatId(userService.getCurrentUser().getId()+"#"+messageRequest.getReceiverId());
        ms.setReceiverId(messageRequest.getReceiverId());
        digitalSignature= Dgst.digitalSignature(text);
        String logUserName= SecurityContextHolder.getContext().getAuthentication().getName();
        String originalHash= Encryption.decode(Certificate.loadUserPrivateKey("src/main/resources/tools/users" + File.separator + logUserName + ".jks",logUserName),digitalSignature);

        int length=text.length();

        if(text.length()==1){
            byte cryptMess[]=Encryption.encrypt(text, SigurnostProjectApplication.secretKey);
            MessagePart parts[]= MessageDivision.messageDivision(cryptMess,messageRequest.getReceiverId(),userService.getCurrentUser().getId(),4);
            producer.sendMessage1(parts[0]);
            producer.sendMessage2(parts[1]);
            producer.sendMessage3(parts[2]);
            producer.sendMessage4(parts[3]);
            try{
                Thread.sleep(1000);
            }catch (Exception e) {
            }
            //ms.setText(Encryption.encrypt(text, SigurnostProjectApplication.secretKey));
        }else if(text.length()==2) {
            int splitIndex = 1;
            String part1 = text.substring(0, splitIndex);
            String part2 = text.substring(splitIndex);

            ImgStegenography.embedText(part2);
            byte cryptMess[]=Encryption.encrypt(part1, SigurnostProjectApplication.secretKey);
            MessagePart parts[]= MessageDivision.messageDivision(cryptMess,messageRequest.getReceiverId(),userService.getCurrentUser().getId(),4);
            producer.sendMessage1(parts[0]);
            producer.sendMessage2(parts[1]);
            producer.sendMessage3(parts[2]);
            producer.sendMessage4(parts[3]);
            try{
                Thread.sleep(1000);
            }catch (Exception e) {
            }
            // ms.setText(Encryption.encrypt(part1, SigurnostProjectApplication.secretKey));
        } else if(text.length()>2){
            int splitIndex = text.length()-2;
            String part1 = text.substring(0, splitIndex);
            String part2 = text.substring(splitIndex);

            ImgStegenography.embedText(part2);
          MessagePart parts[]= MessageDivision.messageDivision(Encryption.encrypt(part1, SigurnostProjectApplication.secretKey),messageRequest.getReceiverId(),userService.getCurrentUser().getId(),4);
          producer.sendMessage1(parts[0]);
          producer.sendMessage2(parts[1]);
          producer.sendMessage3(parts[2]);
          producer.sendMessage4(parts[3]);
          try{
              Thread.sleep(1000);
          }catch (Exception e){

          }
        }

        //ovaj dio radimo kada sa udaljenih servera dobijemo dijelove poruke,spojimo te dijelove
        //uradimo dekripciju te zatim izracunamo hash otisak
        String originalMess=null;
        String dobijeniHash=null;
        try {
            if(text.length()==1){
                byte[] crypt=  MessageDivision.messageConcatenation(RabbitMqConsumer.messageParts,userService.getCurrentUser().getId(),messageRequest.getReceiverId());
                String mess=Encryption.decrypt(crypt,SigurnostProjectApplication.secretKey);
                originalMess=mess;
                ms.setText(crypt);
            }else if(text.length()==2) {
                byte[] crypt=  MessageDivision.messageConcatenation(RabbitMqConsumer.messageParts,userService.getCurrentUser().getId(),messageRequest.getReceiverId());

                String mess1=Encryption.decrypt(crypt,SigurnostProjectApplication.secretKey);
                String mess2=ImgStegenography.extractText(1);
                 String mess=mess1.concat(mess2);
                 originalMess=mess;
                byte[] crypt2=Encryption.encrypt(mess, SigurnostProjectApplication.secretKey);
                ms.setText(crypt2);
            } else if(text.length()>2){
                byte[] crypt=  MessageDivision.messageConcatenation(RabbitMqConsumer.messageParts,userService.getCurrentUser().getId(),messageRequest.getReceiverId());
                String mess1=Encryption.decrypt(crypt,SigurnostProjectApplication.secretKey);
                String mess2=ImgStegenography.extractText(2);
                String mess=mess1.concat(mess2);
                originalMess=mess;
                byte[] crypt2=Encryption.encrypt(mess, SigurnostProjectApplication.secretKey);
                ms.setText(crypt2);
            }


             dobijeniHash = Dgst.hash(originalMess);
            if(!originalHash.equals(dobijeniHash)){
                throw new DigitalSignatureException("Doslo je do izmjene poruke na nekom od servera!");
            }
        }catch (Exception e){
            System.out.println("Doslo je do izmjene poruke na nekom od servera!");
           // e.printStackTrace();
        }
        return messageService.insertMess(ms, MessageRequestFull.class);
    }

    @RequestMapping("/user/{receiverId}")
    public List<MessageDecrypt> getChat(@PathVariable Integer receiverId) throws Exception {
        return messageService.getChat(receiverId);
    }



}
