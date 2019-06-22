package elev;

import driver.Configuration;
import exceptions.ElevatorInvalidDataException;
import gui.ElevatorDisplay;

public enum Direction { //the class is an enum class. No need for toString, automatically returns the enum in print

    UP,
    DOWN,
    IDLE;

    public static Direction determineDirection(int start, int end) throws ElevatorInvalidDataException {
        if (start < 1) {
            throw new ElevatorInvalidDataException("Floors can not be negative");
        }
        if (end > Configuration.NUMBER_FLOORS) {
            throw new ElevatorInvalidDataException("Exceeds number of floors");
        }

        if (end > start) {
            return Direction.UP;
        } else {
            return Direction.DOWN;
        }
    }

    public static ElevatorDisplay.Direction translateDirection(Direction direction) { //changes UP to ElevatorDisplay.Direction.UP
        switch (direction) {
            case UP:
                return ElevatorDisplay.Direction.UP;
            case DOWN:
                return ElevatorDisplay.Direction.DOWN;
            case IDLE:
                return ElevatorDisplay.Direction.IDLE;
            default:
                throw new RuntimeException("default value has not been mapped");
        }
    }

}
