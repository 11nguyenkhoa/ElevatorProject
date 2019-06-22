package exceptions;


public class ElevatorInvalidFileException extends RuntimeException {
    public ElevatorInvalidFileException(String msg) {
        super(msg);
    }

    public ElevatorInvalidFileException(String msg, Throwable e) {
        super(msg, e);
    }
}
