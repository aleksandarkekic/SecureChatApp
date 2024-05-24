package sigurnostbackend.project.models.entities;

import jakarta.persistence.*;
import lombok.*;
import sigurnostbackend.project.base.BaseEntity;

import java.util.Objects;

@Data
@Entity
@Table(name = "user", schema = "sigurnostchat", catalog = "")
public class UserEntity implements BaseEntity<Integer> {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "first_name", nullable = false, length = 80)
    private String firstName;
    @Basic
    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;
    @Basic
    @Column(name = "username", nullable = false, length = 45)
    private String username;
    @Basic
    @Column(name = "password", nullable = false, length = 200)
    private String password;
    @Basic
    @Column(name = "role", nullable = false, length = 45)
    private String role;

}
