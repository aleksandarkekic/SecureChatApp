package sigurnostbackend.project;

import sigurnostbackend.project.configurations.Config;
import sigurnostbackend.project.crypto.Certificate;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
public class SigurnostProjectApplication {
    public static SecretKey secretKey=null;
    public static String path= "src/main/resources/tools" + File.separator + "key"+File.separator+"key.key";

    public static void main(String[] args) {
        SpringApplication.run(SigurnostProjectApplication.class, args);
        Config.config();
        try {
         //   Certificate.createCACertificate();
            byte[] keyBytes = Files.readAllBytes(Paths.get(path));
            secretKey =new SecretKeySpec(keyBytes, "AES");
        }catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }



    }
    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        return modelMapper;
    }
}
