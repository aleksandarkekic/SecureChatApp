package sigurnostbackend.project.models.dto;

import lombok.Data;

@Data
public class Message {
    private Integer id;
    private Integer senderId;
    private Integer receiverId;
    private byte[] text;
    private String createdTime;
    private String chatId;
}
