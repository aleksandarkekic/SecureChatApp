package sigurnostbackend.project.crypto;

import org.springframework.security.core.context.SecurityContextHolder;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

public class Dgst {

    public static   byte[] digitalSignature(String input) {
        try {
            // getInstance() method is called with algorithm SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            // Convert input string to bytes
            byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);

            // Calculate the message digest of the input bytes
            byte[] messageDigest = md.digest(inputBytes);

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            String username= SecurityContextHolder.getContext().getAuthentication().getName();
            PrivateKey privateKey= Certificate.loadUserPrivateKey("src/main/resources/tools/users"+ File.separator+username+".jks",username);
           return Encryption.encode(privateKey,hashtext);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String hash(String message) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        byte[] inputData = message.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("SHA-512");

        byte[] messageDigest = md.digest(inputData);

        // Convert byte array into signum representation
        BigInteger no = new BigInteger(1, messageDigest);

        // Convert message digest into hex value
        String hashtext = no.toString(16);

        // Add preceding 0s to make it 32 bit
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }
}
