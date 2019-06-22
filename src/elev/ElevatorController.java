package elev;

import driver.Configuration;
import exceptions.ElevatorInvalidDataException;

import java.util.ArrayList;

import static driver.Main.getTimeStamp;

public class ElevatorController {
    private static ElevatorController instance;

    private ArrayList<Elevator> elevators = new ArrayList<>();
    private ArrayList<Request> pendingRequests = new ArrayList<>();

    //creates elevator objects based on how many elevators we set
    private ElevatorController() {
        for (int i = 1; i <= Configuration.NUM_ELEV; i++) {
            elevators.add(new Elevator(i));
        }
    }

    public static ElevatorController getInstance() {//Singleton design pattern

        if (instance == null) {
            instance = new ElevatorController();
        }
        return instance;
    }

    private Elevator getElevator(int elevatorNum) {
        return elevators.get(elevatorNum - 1); //-1 since start at 0th index of elevators arraylist
    }


    public void operateElevators() throws ElevatorInvalidDataException {
        for (Elevator elevator : elevators) {
            elevator.move();
        }
    }

    public boolean allElevsDone() {
        int elevDoneCounter = 0;
        boolean elevDone = false;

        for (Elevator elevator : elevators) {
            elevDoneCounter += elevator.getDoneFlag();
        }
        if (elevDoneCounter == Configuration.NUM_ELEV) {
            elevDone = true;
        }
        return elevDone;
    }

    //adds the rider request to elevator
    public void addRiderRequest(int inElevatorNum, Request inRequest) throws ElevatorInvalidDataException {
        if (inElevatorNum < 1) {
            throw new ElevatorInvalidDataException("Elevator ID should be positive");
        }
        ElevatorController.getInstance().getElevator(inElevatorNum).addRequest(inRequest);
    }


    //This adds the starting floor request to elevator
    public void addFloorRequest(int start, Direction d) throws ElevatorInvalidDataException {
        if (start < 1) {
            throw new ElevatorInvalidDataException("Starting Floor should be positive");
        }
        // create request
        Request requestToAdd = new Request(start, d, Request.Type.FLOOR);

        //testing and creating arraylist with: This is start of version 2 of code for algorithm
        ArrayList<ArrayList> elevList = new ArrayList<>();
        for (Elevator elev : elevators) {
            ArrayList<Object> tmpList = new ArrayList<>();
            tmpList.add(elev.getElevatorId());
            tmpList.add(elev.getCurrentFloor());
            tmpList.add(elev.getCurrentDirection());

            elevList.add(tmpList);
        }
        // figure out which elevator to use using our algorithm
        Integer elevatorNum = ElevatorAlgorithmFactory.create().findBestElevator(elevList, requestToAdd); //the factory is choosing which algorithm to use

        // add request to elevator if elevatorNum is not null and its riderCount is less than elevator capacity
        if (elevatorNum != null && getElevator(elevatorNum).getRiderCount() < Configuration.MAX_PERSONS) {
            getElevator(elevatorNum).addRequest(requestToAdd);
            System.out.println(getTimeStamp() + " Elevator: " + elevatorNum + " new floor request to go to Floor " + start + "-" + Direction.determineDirection(getElevator(elevatorNum).getCurrentFloor(), start));
        } else {
            pendingRequests.add(requestToAdd); // add to waiting list if no elevators can service it now
        }
    }

    //adds Pending Requests to available elevators
    public void addPendingRequest(int inElevatorID) throws ElevatorInvalidDataException {
        if (inElevatorID < 1) {
            throw new ElevatorInvalidDataException("Elevator ID should be positive");
        }
        ArrayList<Request> tmpPendingRequestsToRemove = PendingRequestAlgorithmFactory.create().findPending(inElevatorID, pendingRequests); //the factory is choosing which pending request algorithm to use
        if (tmpPendingRequestsToRemove != null) {
            for (Request request : tmpPendingRequestsToRemove) {
                pendingRequests.remove(request);
            }
        }

    }

}