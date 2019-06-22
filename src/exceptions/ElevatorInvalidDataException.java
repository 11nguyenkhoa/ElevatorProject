package exceptions;

public class ElevatorInvalidDataException extends Exception {
    public ElevatorInvalidDataException(String msg) {
        super(msg);
    }

    public ElevatorInvalidDataException(String msg, Throwable e) {
        super(msg, e);
    }
}
