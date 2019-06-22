package elev;

import exceptions.ElevatorInvalidDataException;

public class Request {

    private int floorNum;
    private Direction direction;
    private Type type;

    public enum Type {
        FLOOR,
        RIDER
    }

    //request is UP DOWN request that is sent from Floor to Controller
    public Request(int inFloor, Direction inDirection, Type inType) throws ElevatorInvalidDataException {
        if (inFloor < 1) {
            throw new ElevatorInvalidDataException("Starting Floor should be positive");
        }
        floorNum = inFloor;
        direction = inDirection;
        type = inType;
    }


    public int getFloor() {
        return floorNum;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public String toString() { //called by doing allrequests.values() in elevator class
        return "[" + type + ": " + floorNum + "]";

    }
}
