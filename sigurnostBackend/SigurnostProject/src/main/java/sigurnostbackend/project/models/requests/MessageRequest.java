package sigurnostbackend.project.models.requests;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class MessageRequest {
    private Integer receiverId;
    private String text;
}
