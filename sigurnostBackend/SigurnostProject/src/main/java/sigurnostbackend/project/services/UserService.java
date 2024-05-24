package sigurnostbackend.project.services;

import sigurnostbackend.project.base.CrudService;
import sigurnostbackend.project.models.dto.UserResponse;

public interface UserService extends CrudService<Integer> {
    UserResponse getCurrentUser();
    Integer findIdByUsername(String username);
    String getCurrentRole();
}
