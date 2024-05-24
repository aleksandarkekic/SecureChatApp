package sigurnostbackend.project.crypto;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Encryption {

    public static  byte[] encode(PrivateKey privateKey, String data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] inputData = data.getBytes("UTF-8");
        byte[] outputBytes = cipher.doFinal(inputData);

        // Kriptirane bajtove konvertirajte u Base64 String
       // String encryptedString = Base64.getEncoder().encodeToString(outputBytes);
       // String encryptedString =new String(outputBytes);

        return outputBytes;
    }

    public static String decode(PrivateKey privateKey,  byte[] data) throws Exception {
        PublicKey pubKey = Certificate.getPubKeyFromPrivKey(privateKey);

      //  byte[] inputData = data.getBytes("UTF-8");
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        byte[] bytes = cipher.doFinal(data);
        String hash1= new String(bytes);
        return hash1;

    }
    //saljemo na server byte[] i to primamo nazad
    public static String decrypt(byte[] inputBytes, SecretKey key) throws Exception {

        Key secretKey = new SecretKeySpec(key.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] decryptedContent = cipher.doFinal(inputBytes);
        String output=new String(decryptedContent, "UTF-8");
        return output;

    }
    public static byte[] encrypt(String message, SecretKey key) throws Exception {
        Key secretKey = new SecretKeySpec(key.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] inputData = message.getBytes("UTF-8");
        byte[] outputBytes = cipher.doFinal(inputData);
        return outputBytes;

    }

}
