package exceptions;

public class ElevatorParseException extends RuntimeException {
    public ElevatorParseException(String msg) {
        super(msg);
    }

    public ElevatorParseException(String msg, Throwable e) {
        super(msg, e);
    }
}
