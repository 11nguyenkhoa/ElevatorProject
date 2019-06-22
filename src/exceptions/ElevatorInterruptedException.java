package exceptions;

public class ElevatorInterruptedException extends Exception {
    public ElevatorInterruptedException(String msg) {
        super(msg);
    }

    public ElevatorInterruptedException(String msg, Throwable e) {
        super(msg, e);
    }
}
