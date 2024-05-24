package sigurnostbackend.project.models.requests;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class MessageRequestFull {
    private Integer senderId;
    private Integer receiverId;
    private byte[] text;
    private Timestamp createdTime;
    private String chatId;
}
