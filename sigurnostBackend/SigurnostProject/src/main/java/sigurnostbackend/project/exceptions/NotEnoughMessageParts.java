package sigurnostbackend.project.exceptions;

public class NotEnoughMessageParts extends Exception{
    public NotEnoughMessageParts() {

    }
    public NotEnoughMessageParts(String message){
        super(message);
    }
}
