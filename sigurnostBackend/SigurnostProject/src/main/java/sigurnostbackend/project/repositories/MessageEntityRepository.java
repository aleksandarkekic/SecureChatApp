package sigurnostbackend.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sigurnostbackend.project.models.entities.MessageEntity;
import sigurnostbackend.project.models.entities.UserEntity;

import java.util.List;

public interface MessageEntityRepository extends JpaRepository<MessageEntity, Integer> {
    @Query("SELECT m FROM MessageEntity m WHERE " +
            "(m.senderId = :id1 AND m.receiverId = :id2) OR " +
            "(m.senderId = :id2 AND m.receiverId = :id1)")
    List<MessageEntity> findChat(Integer id1, Integer id2);

}
