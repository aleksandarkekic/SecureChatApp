package sigurnostbackend.project.MessagePartition;

import sigurnostbackend.project.exceptions.NoMessageParts;
import sigurnostbackend.project.exceptions.NotEnoughMessageParts;

import java.util.Arrays;
import java.util.Comparator;

public class MessageDivision {
    public static  MessagePart[] messageDivision(byte[] text,Integer receiverId, Integer senderId, int totalPartss){

        int totalParts = totalPartss;
        int partSize = (int) Math.ceil((double) text.length / totalParts);
        MessagePart[] messageParts = new MessagePart[totalParts];

        for (int i = 0; i < totalParts; i++) {
            int startIndex = i * partSize;
            int endIndex = Math.min(startIndex + partSize, text.length);
            int partLength = endIndex - startIndex;
            byte[] partData = new byte[partLength];
            System.arraycopy(text, startIndex, partData, 0, partLength);

            MessagePart messagePart = new MessagePart();
            messagePart.setNumOfPart(i + 1);
            messagePart.setSenderId(senderId);
            messagePart.setReceiverId(receiverId);
            messagePart.setText(partData);

            messageParts[i] = messagePart;
        }

        return messageParts;
    }

    public static byte[] messageConcatenation(MessagePart []messageParts,Integer senderId, Integer receiverId) throws NoMessageParts, NotEnoughMessageParts {
        if (messageParts == null || messageParts.length == 0) {
            throw new NoMessageParts("Nema dijelova poruke sa servera!");
        }


        for (MessagePart part : messageParts) {
            if (!part.getSenderId().equals(senderId) || !part.getReceiverId().equals(receiverId)) {
                System.out.println("Dijelovi pogresne poruke!");
                return null;
            }
        }


        int totalSize = 0;
        int numbParts=0;
        for (MessagePart part : messageParts) {
            totalSize += part.getText().length;
            numbParts++;
        }
        if(numbParts != 4){
            throw new NotEnoughMessageParts("Nedostaju dijelovi poruke");
        }

        Arrays.sort(messageParts, Comparator.comparingInt(MessagePart::getNumOfPart));
        byte[] concatenatedMessage = new byte[totalSize];
        int currentIndex = 0;
        for (MessagePart part : messageParts) {
            byte[] partData = part.getText();
            System.arraycopy(partData, 0, concatenatedMessage, currentIndex, partData.length);
            currentIndex += partData.length;
        }

        return concatenatedMessage;
    }






}
