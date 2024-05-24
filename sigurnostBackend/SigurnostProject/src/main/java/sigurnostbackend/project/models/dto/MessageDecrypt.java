package sigurnostbackend.project.models.dto;

import lombok.*;

@Data
public class MessageDecrypt {
    private Integer id;
    private Integer senderId;
    private Integer receiverId;
    private String text;
    private String createdTime;
    private String chatId;


}
