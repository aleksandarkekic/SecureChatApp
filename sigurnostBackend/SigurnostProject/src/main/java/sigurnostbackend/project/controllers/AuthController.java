package sigurnostbackend.project.controllers;

import sigurnostbackend.project.crypto.*;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sigurnostbackend.project.SigurnostProjectApplication;
import sigurnostbackend.project.exceptions.NotFoundException;
import sigurnostbackend.project.models.dto.AuthResponse;
import sigurnostbackend.project.models.entities.UserEntity;
import sigurnostbackend.project.models.requests.LoginRequest;
import sigurnostbackend.project.models.requests.RegisterRequest;
import sigurnostbackend.project.repositories.UserEntityRepository;
import sigurnostbackend.project.security.JwtGenerator;
import sigurnostbackend.project.services.UserService;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;
    private final ModelMapper modelMapper;
    private final UserEntityRepository repository;
    private String username ="";
    public static String test="mess";
    public AuthController(AuthenticationManager authenticationManager,
                          UserService userService,
                          PasswordEncoder passwordEncoder,
                          JwtGenerator jwtGenerator,
                          ModelMapper modelMapper, UserEntityRepository repository) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.modelMapper = modelMapper;
        this.repository = repository;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) throws NotFoundException {
        if(repository.existsByUsername(request.getUsername())) {
            return new ResponseEntity<>("Username is taken!", HttpStatus.BAD_REQUEST);
        }
        username= request.getUsername();
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        userService.insert(request, UserEntity.class);
        try {
            Certificate.createUserCert(username);
        }catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
        return new ResponseEntity<>("User registered success!", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request)  {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);

        System.out.println(token);
       // System.out.println("Ulogovani korisnik je: "+SecurityContextHolder.getContext().getAuthentication().getName());
        return new ResponseEntity<>(new AuthResponse(token), HttpStatus.OK);
    }

//    @GetMapping("/proba")
//    public  ResponseEntity<String> proba(){
//        try {
//
//           byte[] encypt= Encryption.encrypt(test, SigurnostProjectApplication.secretKey);
//            byte[] mess= Dgst.digitalSignature(test);
//           System.out.println("digitalni potpis je: "+mess);
//           System.out.println("cypher je: "+encypt);
//           String logUserName= SecurityContextHolder.getContext().getAuthentication().getName();
//           String originalHash= Encryption.decode(Certificate.loadUserPrivateKey("src/main/resources/tools/users" + File.separator + logUserName + ".jks",logUserName),mess);
//           String originalMess=Encryption.decrypt(encypt,SigurnostProjectApplication.secretKey);
//           String dobijeniHash=Dgst.hash(originalMess);
//           System.out.println("originalHash je: "+originalHash);
//           System.out.println("dobijeniHash je: "+dobijeniHash);
//           System.out.println("originalMess je: "+originalMess);
////            byte[] byteArray = { 10, 20, 30, 40, 50 };
////            Stegenography.embedMessageInImageAndSave("marija",Stegenography.outputImagePath);
////           byte[] rez= Stegenography.extractHiddenDataFromImage();
////            System.out.println("dobijena poruka je je "+ new String(rez, StandardCharsets.UTF_8));
////            boolean areEqual = Arrays.equals(encypt, rez);
////
////            if (areEqual) {
////                System.out.println("byteArray1 i byteArray2 su jednaki.");
////            } else {
////                System.out.println("byteArray1 i byteArray2 nisu jednaki.");
////            }
//
//            if(originalHash.equals(dobijeniHash)){
//              System.out.println("Hashevi su isti");
//          }else {
//              System.out.println("Hashevi nisu isti");
//          }
//            ImgStegenography.embedText("text111");
//            ImgStegenography.extractText("text111".length());
//
//        }catch (Exception e){
//            System.out.println(e);
//            e.printStackTrace();
//        }
//        return new ResponseEntity<>( HttpStatus.OK);
//    }
}
