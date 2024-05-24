package sigurnostbackend.project.crypto;

import org.springframework.security.core.context.SecurityContextHolder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImgStegenography {
    public static final String imagePath="src/main/resources/imageOrg/red.png";
    public static final String outputImagePath="src/main/resources/ImagesStegenography";

    public static BufferedImage embedText( String text) throws IOException {
        BufferedImage image = ImageIO.read(new File(imagePath));
        int bitMask = 0x00000001;	// define the mask bit used to get the digit
        int bit;				// define a integer number to represent the ASCII number of a character
        int x = 0;				// define the starting pixel x
        int y = 0;				// define the starting pixel y
        for(int i = 0; i < text.length(); i++) {
            bit = (int) text.charAt(i);		// get the ASCII number of a character
            for(int j = 0; j < 8; j++) {
                int flag = bit & bitMask;	// get 1 digit from the character
                if(flag == 1) {
                    if(x < image.getWidth()) {
                        image.setRGB(x, y, image.getRGB(x, y) | 0x00000001); 	// store the bit which is 1 into a pixel's last digit
                        x++;
                    }
                    else {
                        x = 0;
                        y++;
                        image.setRGB(x, y, image.getRGB(x, y) | 0x00000001); 	// store the bit which is 1 into a pixel's last digit
                    }
                }
                else {
                    if(x < image.getWidth()) {
                        image.setRGB(x, y, image.getRGB(x, y) & 0xFFFFFFFE);	// store the bit which is 0 into a pixel's last digit
                        x++;
                    }
                    else {
                        x = 0;
                        y++;
                        image.setRGB(x, y, image.getRGB(x, y) & 0xFFFFFFFE);	// store the bit which is 0 into a pixel's last digit
                    }
                }
                bit = bit >> 1;				// get the next digit from the character
            }
        }

        // save the image which contains the secret information to another image file
        try {
            File outputfile = new File(outputImagePath +File.separator+ SecurityContextHolder.getContext().getAuthentication().getName()+".png");
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {

        }
        return image;
    }

    // extract secret information/Text from a "cover image"
    public static String extractText( int length) throws IOException {
        BufferedImage image = ImageIO.read( new File(outputImagePath +File.separator+ SecurityContextHolder.getContext().getAuthentication().getName()+".png"));

        System.out.print("Extracting: ");
        int bitMask = 0x00000001;	// define the mask bit used to get the digit
        int x = 0;					// define the starting pixel x
        int y = 0;					// define the starting pixel y
        int flag;
        char[] c = new char[length] ;	// define a character array to store the secret information
        String hiddenText=null;
        for(int i = 0; i < length; i++) {
            int bit = 0;

            // 8 digits form a character
            for(int j = 0; j < 8; j++) {
                if(x < image.getWidth()) {
                    flag = image.getRGB(x, y) & bitMask;	// get the last digit of the pixel
                    x++;
                }
                else {
                    x = 0;
                    y++;
                    flag = image.getRGB(x, y) & bitMask;	// get the last digit of the pixel
                }

                // store the extracted digits into an integer as a ASCII number
                if(flag == 1) {
                    bit = bit >> 1;
                    bit = bit | 0x80;
                }
                else {
                    bit = bit >> 1;
                }
            }
            c[i] = (char) bit;	// represent the ASCII number by characters
            hiddenText=new String(c);
        }
        Path path = Paths.get(outputImagePath +File.separator+ SecurityContextHolder.getContext().getAuthentication().getName()+".png");
        if (Files.exists(path)) {
            Files.delete(path);
        }
        return hiddenText;
    }

}
