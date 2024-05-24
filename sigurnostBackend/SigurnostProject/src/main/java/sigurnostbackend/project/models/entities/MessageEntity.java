package sigurnostbackend.project.models.entities;

import jakarta.persistence.*;
import lombok.*;
import sigurnostbackend.project.base.BaseEntity;

import java.sql.Timestamp;
import java.util.Objects;
@Data
@Entity
@Table(name = "message", schema = "sigurnostchat", catalog = "")
public class MessageEntity implements BaseEntity<Integer> {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "sender_id", nullable = false)
    private Integer senderId;
    @Basic
    @Column(name = "receiver_id", nullable = false)
    private Integer receiverId;
    @Basic
    @Column(name = "text", nullable = false, length = 45)
    private byte[] text;
    @Basic
    @Column(name = "created_time", nullable = false, length = 45)
    private Timestamp createdTime;
    @Basic
    @Column(name = "chat_id", nullable = false, length = 45)
    private String chatId;

}
