package sigurnostbackend.project.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sigurnostbackend.project.base.CrudJpaService;
import sigurnostbackend.project.models.dto.UserResponse;
import sigurnostbackend.project.models.entities.UserEntity;
import sigurnostbackend.project.repositories.UserEntityRepository;
import sigurnostbackend.project.services.UserService;

@Service
public class UserServiceImpl extends CrudJpaService<UserEntity, Integer> implements UserService {

    private final UserEntityRepository repository;

        public UserServiceImpl(UserEntityRepository repository, ModelMapper modelMapper){
            super(repository, modelMapper, UserEntity.class);
            this.repository = repository;
        }

    @Override
    public UserResponse getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("Ulogovani korisnik je:" +username);
        return super.getModelMapper().map(repository.findByUsername(username), UserResponse.class);
    }

    @Override
    public Integer findIdByUsername(String username) {
        return repository.findIdByUsername(username);
    }
    @Override
    public String getCurrentRole(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return repository.findRoleByUsername(username);
    }

}
