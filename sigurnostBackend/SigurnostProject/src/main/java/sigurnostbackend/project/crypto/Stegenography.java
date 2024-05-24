package sigurnostbackend.project.crypto;

import org.springframework.security.core.context.SecurityContextHolder;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Stegenography {
        public static final String imagePath="src/main/resources/imageOrg/red.jpeg";
        public static final String outputImagePath="src/main/resources/ImagesStegenography";

    public static void embedMessageInImageAndSave( String data, String outputImagePath) throws IOException, IOException{
    BufferedImage originalImage = ImageIO.read(new File(imagePath));
        byte[] dataToHide=data.getBytes(StandardCharsets.UTF_8);
        // Kopiraj originalnu sliku kako bismo sačuvali originalnu verziju netaknutom
        BufferedImage copiedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = copiedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, null);
        g.dispose();

        // Provjeri veličinu kopirane slike za skrivanje podataka
        if (dataToHide.length * 8 > copiedImage.getWidth() * copiedImage.getHeight()) {
            throw new IllegalArgumentException("Slika je premala za skrivanje podataka.");
        }

        int dataIndex = 0;

        // Iteriraj kroz piksele kopirane slike i skrivaj podatke
        for (int y = 0; y < copiedImage.getHeight(); y++) {
            for (int x = 0; x < copiedImage.getWidth(); x++) {
                int pixel = copiedImage.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xFF;
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;

                if (dataIndex < dataToHide.length) {
                    int dataByte = dataToHide[dataIndex];

                    for (int bit = 0; bit < 8; bit++) {
                        int dataBit = (dataByte >> bit) & 1;
                        red = (red & 0xFE) | dataBit;
                        dataIndex++;

                        if (dataIndex >= dataToHide.length) {
                            break;
                        }
                    }
                }

                int encodedPixel = (alpha << 24) | (red << 16) | (green << 8) | blue;
                copiedImage.setRGB(x, y, encodedPixel);
            }
        }
    // Spremi stegiranu sliku na disku
    File outputImageFile = new File(outputImagePath +File.separator+ SecurityContextHolder.getContext().getAuthentication().getName()+".png");
        if (!outputImageFile.exists()) {
            // Ako datoteka ne postoji, stvori je
            Files.createFile(outputImageFile.toPath());
        }
        ImageIO.write(copiedImage, "png", outputImageFile);

}

//    public static byte[] extractHiddenDataFromImage() throws IOException {
//        // Učitaj sliku iz PNG datoteke
//        BufferedImage image = ImageIO.read( new File(outputImagePath +File.separator+ SecurityContextHolder.getContext().getAuthentication().getName()+".png"));
//
//        int dataIndex = 0;
//        int dataByte = 0;
//        byte[] extractedData = new byte[image.getWidth() * image.getHeight()];
//
//        for (int y = 0; y < image.getHeight(); y++) {
//            for (int x = 0; x < image.getWidth(); x++) {
//                int pixel = image.getRGB(x, y);
//                int red = (pixel >> 16) & 0xFF;
//
//                dataByte = (dataByte << 1) | (red & 1);
//                dataIndex++;
//
//                if (dataIndex % 8 == 0) {
//                    extractedData[dataIndex / 8 - 1] = (byte) dataByte;
//                    dataByte = 0;
//                }
//            }
//        }
//        Path path = Paths.get(outputImagePath +File.separator+ SecurityContextHolder.getContext().getAuthentication().getName()+".png");
//        if (Files.exists(path)) {
//            Files.delete(path);
//        }
//        // Vraća izvučene podatke
//        return extractedData;
//    }
    public static byte[] extractHiddenDataFromImage() throws IOException {
        // Učitaj sliku iz PNG datoteke
        BufferedImage image = ImageIO.read( new File(outputImagePath +File.separator+ SecurityContextHolder.getContext().getAuthentication().getName()+".png"));

        int dataIndex = 0;
        int dataByte = 0;
        List<Byte> extractedDataList = new ArrayList<>();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                int red = (pixel >> 16) & 0xFF;

                dataByte = (dataByte << 1) | (red & 1);
                dataIndex++;

                if (dataIndex % 8 == 0) {
                    extractedDataList.add((byte) dataByte);
                    dataByte = 0;
                }
            }
        }

        // Konvertuj listu bajtova u niz bajtova
        byte[] extractedData = new byte[extractedDataList.size()];
        for (int i = 0; i < extractedDataList.size(); i++) {
            extractedData[i] = extractedDataList.get(i);
        }
        Path path = Paths.get(outputImagePath +File.separator+ SecurityContextHolder.getContext().getAuthentication().getName()+".png");
        if (Files.exists(path)) {
            Files.delete(path);
       }
        // Vraća izvučene podatke
        return extractedData;
    }

}







