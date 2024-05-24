package sigurnostbackend.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sigurnostbackend.project.models.entities.UserEntity;

import java.util.Optional;

public interface UserEntityRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);
    Integer findIdByUsername(String username);
    UserEntity getUserEntitiesById(Integer id);
    Boolean existsByUsername(String username);

    @Query("select u.role from UserEntity u where u.username = :username")
    String findRoleByUsername(String username);

}
