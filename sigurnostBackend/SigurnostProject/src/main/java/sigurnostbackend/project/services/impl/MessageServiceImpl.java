package sigurnostbackend.project.services.impl;

import sigurnostbackend.project.controllers.MessageController;
import sigurnostbackend.project.crypto.Encryption;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import sigurnostbackend.project.SigurnostProjectApplication;
import sigurnostbackend.project.base.CrudJpaService;
import sigurnostbackend.project.models.dto.Message;
import sigurnostbackend.project.models.dto.MessageDecrypt;
import sigurnostbackend.project.models.entities.MessageEntity;

import sigurnostbackend.project.models.requests.MessageRequestFull;
import sigurnostbackend.project.repositories.MessageEntityRepository;
import sigurnostbackend.project.services.MessageService;
import sigurnostbackend.project.services.UserService;

import java.util.ArrayList;
import java.util.List;


@Service
public class MessageServiceImpl extends CrudJpaService<MessageEntity, Integer> implements MessageService {

    private MessageEntityRepository repository;
    private UserService userService;
    @PersistenceContext
    private EntityManager entityManager;
    private final ModelMapper modelMapper;

    public MessageServiceImpl(MessageEntityRepository repository, ModelMapper modelMapper, UserService userService){
        super(repository, modelMapper, MessageEntity.class);
        this.repository = repository;
        this.userService=userService;
        this.modelMapper=modelMapper;
    }
    @Override
    public List<MessageDecrypt> getChat(Integer receiverId) throws Exception {
        Integer currentUserId = userService.getCurrentUser().getId();
        List<Message> messages=repository
                .findChat(currentUserId,receiverId)
                .stream()
                .map(m -> modelMapper.map(m,Message.class))
                .toList();
        List<MessageDecrypt> decryptedMessages = new ArrayList<>();

        for (Message message : messages) {
            MessageDecrypt decryptedMessage = new MessageDecrypt();
            decryptedMessage.setId(message.getId());
            decryptedMessage.setSenderId(message.getSenderId());
            decryptedMessage.setReceiverId(message.getReceiverId());
            decryptedMessage.setCreatedTime(message.getCreatedTime());
            decryptedMessage.setChatId(message.getChatId());

            // Pretvaranje byte[] u String pomoću metode "encrypt"
            byte[] encryptedData = message.getText();
            String decryptedText = Encryption.decrypt(encryptedData, SigurnostProjectApplication.secretKey); // Pretpostavljeno ime metode

            decryptedMessage.setText(decryptedText);

            decryptedMessages.add(decryptedMessage);
        }
        return decryptedMessages;
    }
    public MessageRequestFull insertMess(MessageRequestFull object, Class<MessageRequestFull> resultDtoClass) {
        MessageEntity entity = modelMapper.map(object,MessageEntity.class);
        entity.setId(null);
        entity = repository.saveAndFlush(entity);
        entityManager.refresh(entity);
        return modelMapper.map(entity ,resultDtoClass);
    }

}
