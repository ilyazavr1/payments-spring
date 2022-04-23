package ua.epma.paymentsspring.model.excwption;

public class InvalidCardNumberException extends Exception{
    public InvalidCardNumberException() {

    }
    public InvalidCardNumberException(String message) {
        super(message);
    }


    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
