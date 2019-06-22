package building;

import driver.Configuration;
import exceptions.ElevatorInvalidDataException;

public class Person {

    private String personId; //instance variable
    private int start;
    private int end;
    private long startWaitTime;
    private long endWaitTime;
    private long startRideTime;
    private long endRideTime;

    //constructor
    public Person(String personIdIn, int inStart, int inEnd) throws ElevatorInvalidDataException {
        if (inStart < 1) {
            throw new ElevatorInvalidDataException("Floors can not be negative");
        }
        if (end > Configuration.NUMBER_FLOORS) {
            throw new ElevatorInvalidDataException("Exceeds number of floors");
        }
        personId = personIdIn;
        start = inStart;
        end = inEnd;
    }

    public void setStartWaitTime(long inTime) throws ElevatorInvalidDataException {
        if (inTime < 0) {
            throw new ElevatorInvalidDataException("time cannot be negative");
        }
        startWaitTime = getSeconds(inTime);
    }


    public void setEndWaitTime(long inTime) throws ElevatorInvalidDataException {
        if (inTime < 0) {
            throw new ElevatorInvalidDataException("time cannot be negative");
        }
        endWaitTime = getSeconds(inTime);
    }

    public void setStartRideTime(long inTime) throws ElevatorInvalidDataException {
        if (inTime < 0) {
            throw new ElevatorInvalidDataException("time cannot be negative");
        }
        startRideTime = getSeconds(inTime);
    }

    public void setEndRideTime(long inTime) throws ElevatorInvalidDataException {
        if (inTime < 0) {
            throw new ElevatorInvalidDataException("time cannot be negative");
        }
        endRideTime = getSeconds(inTime);
    }

    public long getWaitTime() {
        long waitTime = endWaitTime - startWaitTime;
        return waitTime;
    }

    public long getRideTime() {
        long rideTime = endRideTime - startRideTime;
        return rideTime;
    }

    public long getTotalTime() {
        long totalTime = getWaitTime() + getRideTime();
        return totalTime;
    }


    private long getSeconds(long inTime) {
        long now = inTime;
        long endSeconds = now / 1000;
        return endSeconds;
    }


    public String getPersonId() {
        return personId;
    }

    public int getDestinationFloor() {
        return end;
    }

    public int getPersonStartFloor() {
        return start;
    }

    public String toString() {
        return personId;
    }
}
