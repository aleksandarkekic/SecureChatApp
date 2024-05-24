package sigurnostbackend.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sigurnostbackend.project.exceptions.NotFoundException;
import sigurnostbackend.project.models.dto.User;
import sigurnostbackend.project.models.dto.UserResponse;
import sigurnostbackend.project.services.UserService;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/current-user")
    public ResponseEntity<UserResponse> getCurrentUser() {
        UserResponse currentUser = userService.getCurrentUser();
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }
    @GetMapping("/current-role")
    public String getCurrentRole() {
        return userService.getCurrentRole();
    }


    @GetMapping("/IdUsername")
    public ResponseEntity<Integer> findIdByUsername(String username){
       Integer id=userService.findIdByUsername(username);
       return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public List<UserResponse> findAll() throws NotFoundException {
        List<UserResponse> users=userService.findAll(UserResponse.class);
        return users;


    }
}