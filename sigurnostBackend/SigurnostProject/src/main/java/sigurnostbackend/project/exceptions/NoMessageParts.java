package sigurnostbackend.project.exceptions;

public class NoMessageParts extends Exception{
    public NoMessageParts() {

    }
    public NoMessageParts(String message){
        super(message);
    }
}
