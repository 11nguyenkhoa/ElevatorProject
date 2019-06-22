package exceptions;

public class ElevatorRuntimeException extends RuntimeException {
    public ElevatorRuntimeException(String msg) {
        super(msg);
    }

    public ElevatorRuntimeException(String msg, Throwable e) {
        super(msg, e);
    }
}
