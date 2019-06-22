package building;

import driver.Configuration;
import exceptions.ElevatorInvalidDataException;

import java.util.ArrayList;

public class Floor {

    private int floorNum;
    private ArrayList<Person> waitingForElevator = new ArrayList<>();
    private ArrayList<Person> completedPerson = new ArrayList<>();


    public Floor(int inFloorNum) throws ElevatorInvalidDataException {
        if (inFloorNum < 1) {
            throw new ElevatorInvalidDataException("Floor number has to be positive");
        }
        if (inFloorNum > Configuration.NUMBER_FLOORS) {
            throw new ElevatorInvalidDataException("Floor number must not exceed building number of floors");
        }

        floorNum = inFloorNum;
    }

    public void addArrivedPerson(Person p) {
        completedPerson.add(p);
    }

    public void addWaitingPerson(Person p) {
        waitingForElevator.add(p);
    }

    public int getFloorNum() {
        return floorNum;
    }


}
