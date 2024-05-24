package sigurnostbackend.project.services;

import sigurnostbackend.project.base.CrudService;
import sigurnostbackend.project.models.dto.Message;
import sigurnostbackend.project.models.dto.MessageDecrypt;
import sigurnostbackend.project.models.requests.MessageRequestFull;

import java.util.List;

public interface MessageService extends CrudService<Integer> {
     List<MessageDecrypt> getChat(Integer receiverId) throws Exception;
     MessageRequestFull insertMess(MessageRequestFull object, Class<MessageRequestFull> resultDtoClass) ;

     }
