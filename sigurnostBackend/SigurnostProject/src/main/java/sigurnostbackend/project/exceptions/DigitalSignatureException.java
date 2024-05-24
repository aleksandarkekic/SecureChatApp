package sigurnostbackend.project.exceptions;

public class DigitalSignatureException extends Exception{
    public DigitalSignatureException() {

    }
    public DigitalSignatureException(String message){
        super(message);
    }
}
