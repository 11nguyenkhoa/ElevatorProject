package elev;

import building.Building;
import building.Person;
import driver.Configuration;
import exceptions.ElevatorInvalidDataException;
import exceptions.ElevatorRuntimeException;
import gui.ElevatorDisplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import static driver.Main.getInitTime;
import static driver.Main.getTimeStamp;


public class Elevator {
    private int id;
    private int currentFloor;
    private Direction direction;
    private ArrayList<Person> riders = new ArrayList<>();
    private ArrayList<Person> ridersToRemove = new ArrayList<>();
    private ArrayList<Person> peopleToRemove = new ArrayList<>(); //these are the people to remove from the allPendingPeople list in Building object
    private int riderCount = 0;
    private int idleCount = 0;
    private int doorTimer = 0;
    private int floorTimer = 0;
    private int elevDoneFlag = 0;
    private boolean bringElevatorDown = false;
    private static final int TIME_SLICER = 1000;

    private HashMap<Integer, Request> allRequest = new HashMap<>();

    public Elevator(int idIn) { //being added in ElevatorController class
        id = idIn;
        currentFloor = 1; //sets the current floor for every elevator at 1
        direction = Direction.IDLE;
    }

    public int getElevatorId() {
        return id;
    }

    private void setElevatorFloor(int inFloor) {
        currentFloor = inFloor;
    }

    public void addRequest(Request request) {
        //if the request is already there for that floor, skip, otherwise, add to to Hashmap.
        if (allRequest.get(request.getFloor()) != null) {
            System.out.println("Request for that floor already exists so skipping adding it again");
        } else {
            allRequest.put(request.getFloor(), request); //key
        }
    }

    //checks to see if the Request is Pending or Not aka the Elevator should visit that specific request floor/destination floor
    private boolean isRequestPending() {
        Set<Integer> floorsWithRequest = allRequest.keySet(); //keyset returns the values so its allfloors with a pending request

        if (floorsWithRequest.isEmpty()) {
            return false;
        }

        for (int floor : floorsWithRequest) {//if there are requests on floors greater than or less than current floor, continue moving

            // IDLE
            if (direction == Direction.IDLE) { //example is after elevator paused for 5 seconds, we decided to throw in a request on the floor the elevator is currently at
                return true;
            }

            // UP
            if (direction == Direction.UP) {//you have no more pending requests above you
                if (floor > currentFloor) {
                    return true;
                }
            }

            // DOWN
            if (direction == Direction.DOWN) {
                if (floor < currentFloor) {
                    return true;
                }
            }
        }

        return false;
    }

    private Direction determineElevatorDirectionAgain() { //will call this to set direction after isRequestPending returns false
        Set<Integer> floorsWithRequest = allRequest.keySet();
        for (int floor : floorsWithRequest) {
            try {
                return Direction.determineDirection(currentFloor, floor);
            } catch (ElevatorInvalidDataException e) {
                throw new ElevatorRuntimeException("Elevator has requests with invalid data.", e);
            }
        }

        return Direction.IDLE;
    }


    public void move() throws ElevatorInvalidDataException {
        System.out.println("Elevator " + getElevatorId() + " The current floor is :" + currentFloor);
        ElevatorDisplay.getInstance().updateElevator(id, currentFloor, riderCount, Direction.translateDirection(direction)); //translate direction turns UP into ElevatorDisplay.Direction.UP

        //first check if there is a request pending.
        if (isRequestPending()) {
            idleCount = 0; //resets idlecount to 0 if there is a request
            bringElevatorDown = false;
            if (allRequest.get(currentFloor) != null) { //If there's a request on the currentfloor, move people in and out.
                if (doorTimer == 0) {
                    System.out.println(getTimeStamp() + " Elevator " + id + " has arrived at Floor " + currentFloor);
                    System.out.println(getTimeStamp() + " Elevator " + id + " Doors Open");
                    ElevatorDisplay.getInstance().openDoors(getElevatorId());

                    movePeopleOut();
                    movePeopleIn();
                    doorTimer += TIME_SLICER;
                } else if (doorTimer > 0 && doorTimer < Configuration.DOOR_TIME) { //if we still haven't passed the elevator door time, add the Time slicer(default 1 second).
                    doorTimer += TIME_SLICER;
                } else { //if we pass the elevator door time, we close the door and remove the currentFloor
                    doorTimer = 0;
                    ElevatorDisplay.getInstance().closeDoors(getElevatorId());
                    System.out.println(getTimeStamp() + " Elevator " + id + " Doors Close");
                    allRequest.remove(currentFloor); // request finished so remove the floor from the request hashmap
                }

                //If the request isn't on the Current Floor, we do this.
            } else {
                if (direction == Direction.IDLE) {
                    direction = determineElevatorDirectionAgain();
                    System.out.println(getTimeStamp() + " Elevator " + id + " checking direction " + direction);

                }
                //We do the same thing for floor time as we did for door time above.
                if (floorTimer < Configuration.FLOOR_TIME) {
                    floorTimer += TIME_SLICER;
                }
                //continuously goes direction until direction change, which is shown via gui in the updateElevator method in beginning of move method. i.e. continuously setting the elevator floor to +1 or -1
                if (floorTimer >= Configuration.FLOOR_TIME) {
                    floorTimer = 0; //this resets floorTimer to 0
                    //put this in if statement in case its the last loop, request is empty and don't want it change floor on next loop
                    if (direction == Direction.UP) {
                        System.out.println(getTimeStamp() + " Elevator " + id + " is moving from Floor " + currentFloor + " to Floor " + (currentFloor + 1) + " [Current Requests: " + allRequest.values() + " ]");
                        setElevatorFloor(currentFloor + 1); //set the Floor of elevator to that of the next floor.
                    } else if (direction == Direction.DOWN) {
                        System.out.println(getTimeStamp() + " Elevator " + id + " is moving from Floor " + currentFloor + " to Floor " + (currentFloor - 1) + " [Current Requests: " + allRequest.values() + " ]");
                        setElevatorFloor(currentFloor - 1); //set the Floor of elevator to that of the next floor.
                    } else {
                        System.out.println("Elevator Idling " + id);
                    }
                } else {
                    System.out.println("Moving between floor: " + floorTimer + " Elevator " + id);
                }

            }
        } else {
            direction = Direction.IDLE;
            ElevatorController.getInstance().addPendingRequest(getElevatorId());//Since there's no request pending, we should add the first request in pendingRequests list
            idleCount += TIME_SLICER;
            if (idleCount == Configuration.IDLE_TIME) {//once we hit the idle time, we bring the Elevator Down
                bringElevatorDown = true;
            }
            if (bringElevatorDown) {
                moveIdleElevatorDown();
            }
        }
    }


    private void movePeopleIn() throws ElevatorInvalidDataException {
        boolean continueProblem = false;
        for (Person p : riders) {
            if (currentFloor == p.getPersonStartFloor()) {
                continueProblem = true;
            }
        }
        if (continueProblem) {
            return;
        }
        for (Person p : Building.getInstance().getAllPeopleList()) {
            if (currentFloor == p.getPersonStartFloor()) {
                Direction riderDirection = Direction.determineDirection(p.getPersonStartFloor(), p.getDestinationFloor());
                peopleToRemove.add(p);
                riders.add(p);
                riderCount += 1; //this is for the rider number
                p.setEndWaitTime(System.currentTimeMillis() - getInitTime()); //signals the person is done waiting for Elev
                p.setStartRideTime(System.currentTimeMillis() - getInitTime()); //signals person is starting the ride wait time
                ElevatorController.getInstance().addRiderRequest(getElevatorId(), new Request(p.getDestinationFloor(), riderDirection, Request.Type.RIDER));
                System.out.println(getTimeStamp() + " Person " + p + " has left Floor " + currentFloor + " [Riders " + riders + "]");
                System.out.println(getTimeStamp() + " Person " + p + " entered Elevator " + id + " [Riders " + riders + "]");
                System.out.println(getTimeStamp() + " Elevator " + id + " rider request made for Floor " + p.getDestinationFloor() + " [Current Requests: " + allRequest.values() + " ]");
            }
        }
        //this is to remove the persons who have already completed so we won't duplicate their requests
        for (Person p : peopleToRemove) {
            Building.getInstance().removePerson(p);
        }
    }

    private void movePeopleOut() throws ElevatorInvalidDataException {
        boolean continueProb = false;
        for (Person p : riders) {
            if (currentFloor == p.getDestinationFloor()) {
                continueProb = true;
            }
        }
        if (continueProb == false) {
            return;
        }

        for (Person p : riders) {
            if (currentFloor == p.getDestinationFloor()) {
                ridersToRemove.add(p);
                riderCount -= 1;
                System.out.println(getTimeStamp() + " person " + p + " has left Elevator " + id + " [Current Requests: " + allRequest.values() + " ]");
                System.out.println(getTimeStamp() + " person " + p + " has entered Floor " + currentFloor + " [Current Requests: " + allRequest.values() + " ]");
                p.setEndRideTime(System.currentTimeMillis() - getInitTime());
            }
        }
        for (Person p : ridersToRemove) {
            riders.remove(p);
        }
    }


    private void moveIdleElevatorDown() {
        if (currentFloor == 1) {
            idleCount = 0;
            bringElevatorDown = false;
            elevDoneFlag = 1;
            return;
        } else elevDoneFlag = 0;


        if (floorTimer < Configuration.FLOOR_TIME) {
            floorTimer += TIME_SLICER;
        }

        if (floorTimer >= Configuration.FLOOR_TIME) {
            floorTimer = 0; //this resets floorTimer to 0

            System.out.println(getTimeStamp() + " Elevator " + id + " is moving from Floor " + currentFloor + " to Floor " + (currentFloor - 1) + " [Current Requests: " + allRequest.values() + " ]");
            ElevatorDisplay.getInstance().updateElevator(getElevatorId(), currentFloor - 1, riderCount, ElevatorDisplay.Direction.DOWN);
            currentFloor -= 1; //subtract one from floor to bring elevator down
        }
    }

    public int getCurrentFloor() {
        return currentFloor;

    }

    public int getRiderCount() {
        return riderCount;
    }

    public int getDoneFlag() {
        return elevDoneFlag;
    }

    public Direction getCurrentDirection() {
        return direction;
    }

}