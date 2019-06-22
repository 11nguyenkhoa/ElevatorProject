package elev;

import exceptions.ElevatorInvalidDataException;

import java.util.ArrayList;

public interface ElevatorAlgorithm {

    Integer findBestElevator(ArrayList<ArrayList> elevators, Request requestToAdd) throws ElevatorInvalidDataException;


}
