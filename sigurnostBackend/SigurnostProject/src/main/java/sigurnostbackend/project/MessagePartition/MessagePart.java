package sigurnostbackend.project.MessagePartition;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessagePart {
    private Integer numOfPart;
    private Integer senderId;
    private Integer receiverId;
    private byte[] text;
}
