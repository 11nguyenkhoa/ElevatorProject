/**
 * @author Khoa Nguyen
 **/
package driver;

import building.Building;
import building.Person;
import elev.Direction;
import elev.ElevatorController;
import exceptions.ElevatorInterruptedException;
import exceptions.ElevatorInvalidDataException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;


public class Main {
    private static long initTime = System.currentTimeMillis();
    private static int personCounter = 1; //starts at 1 so first person is P1, then next is P2. Used by addPerson method
    private static Random randomObj = new Random(123);
    private static final int TIME_SLICER = 1000; //1000 milliseconds is equal to 1 second

    public static void main(String[] args) throws ElevatorInterruptedException, ElevatorInvalidDataException {
        Building.getInstance(); //creates an Instance of Building Object, with 20 floors and 4 elevators as specified in the json file
        part2();
    }

    private static void test1() throws ElevatorInterruptedException, ElevatorInvalidDataException {
        for (int i = 0; i < 10; i++) {
            if (i == 3) {
                addPerson(5, 13);
                addPerson(6, 11);
                addPerson(7, 14);
            }
            ElevatorController.getInstance().operateElevators();
            System.out.println("------------------------------------------------------------------");
            try {
                Thread.sleep(TIME_SLICER);
            } catch (InterruptedException e) {
                throw new ElevatorInterruptedException("Program Interrupted", e);
            }
        }
        while (ElevatorController.getInstance().allElevsDone() == false) {
            ElevatorController.getInstance().operateElevators();
            System.out.println("------------------------------------------------------------------");
            try {
                Thread.sleep(TIME_SLICER);
            } catch (InterruptedException e) {
                throw new ElevatorInterruptedException("Program Interrupted", e);
            }
        }
        printPeopleStats();
    }


    private static void test2() throws ElevatorInterruptedException, ElevatorInvalidDataException {
        for (int i = 0; i < 25; i++) {
            switch (i) {
                case 0:
                    addPerson(1, 9);
                    break;
                case 4:
                    addPerson(20, 1);
                    break;
                case 5:
                    addPerson(5, 1);
                    break;
                case 6:
                    addPerson(4, 1);
                    break;
                case 7:
                    addPerson(3, 6);
            }
            ElevatorController.getInstance().operateElevators();
            System.out.println("------------------------------------------------------------------");
            try {
                Thread.sleep(TIME_SLICER);
            } catch (InterruptedException e) {
                throw new ElevatorInterruptedException("Program Interrupted", e);
            }
        }
        while (ElevatorController.getInstance().allElevsDone() == false) {
            ElevatorController.getInstance().operateElevators();
            System.out.println("------------------------------------------------------------------");
            try {
                Thread.sleep(TIME_SLICER);
            } catch (InterruptedException e) {
                throw new ElevatorInterruptedException("Program Interrupted", e);
            }
        }
        printPeopleStats();

    }

    private static void test3() throws ElevatorInterruptedException, ElevatorInvalidDataException {
        for (int i = 0; i < 60; i++) {
            switch (i) {
                case 0:
                    addPerson(20, 1);
                    break;
                case 25:
                    addPerson(10, 1);
            }
            ElevatorController.getInstance().operateElevators();
            System.out.println("------------------------------------------------------------------");
            try {
                Thread.sleep(TIME_SLICER);
            } catch (InterruptedException e) {
                throw new ElevatorInterruptedException("Program Interrupted", e);
            }
        }
        while (ElevatorController.getInstance().allElevsDone() == false) {
            ElevatorController.getInstance().operateElevators();
            System.out.println("------------------------------------------------------------------");
            try {
                Thread.sleep(TIME_SLICER);
            } catch (InterruptedException e) {
                throw new ElevatorInterruptedException("Program Interrupted", e);
            }
        }
        printPeopleStats();
    }

    private static void test4() throws ElevatorInterruptedException, ElevatorInvalidDataException {
        for (int i = 0; i < 65; i++) {
            switch (i) {
                case 0:
                    addPerson(1, 10); //elev 1
                    break;
                case 5:
                    addPerson(8, 17); //elev 1
                    break;
                case 6:
                    addPerson(1, 9); //elev 4
                    break;
                case 25:
                    addPerson(3, 1); //elev 4

            }
            ElevatorController.getInstance().operateElevators();
            System.out.println("------------------------------------------------------------------");
            try {
                Thread.sleep(TIME_SLICER);
            } catch (InterruptedException e) {
                throw new ElevatorInterruptedException("Program Interrupted", e);
            }
        }
        while (ElevatorController.getInstance().allElevsDone() == false) {
            ElevatorController.getInstance().operateElevators();
            System.out.println("------------------------------------------------------------------");
            try {
                Thread.sleep(TIME_SLICER);
            } catch (InterruptedException e) {
                throw new ElevatorInterruptedException("Program Interrupted", e);
            }
        }
        printPeopleStats();
    }


    private static void part2() throws ElevatorInterruptedException, ElevatorInvalidDataException {
        for (int i = 0; i < Configuration.DURATION_TIME; i++) {
            if (i % Configuration.CREATION_RATE == 0) {
                int startFloor = (int) (randomObj.nextDouble() * Configuration.NUMBER_FLOORS + 1);
                int endFloor = (int) (randomObj.nextDouble() * Configuration.NUMBER_FLOORS + 1);
                while (endFloor == startFloor) {
                    endFloor = (int) (randomObj.nextDouble() * Configuration.NUMBER_FLOORS + 1);
                }
                addPerson(startFloor, endFloor);
            }
            ElevatorController.getInstance().operateElevators();
            System.out.println("------------------------------------------------------------------");
            try {
                Thread.sleep(TIME_SLICER);
            } catch (InterruptedException e) {
                throw new ElevatorInterruptedException("Program Interrupted", e);
            }
        }

        while (ElevatorController.getInstance().allElevsDone() == false) {
            ElevatorController.getInstance().operateElevators();
            System.out.println("------------------------------------------------------------------");
            try {
                Thread.sleep(TIME_SLICER);
            } catch (InterruptedException e) {
                throw new ElevatorInterruptedException("Program Interrupted", e);
            }
        }
        printPeopleStats();


    }


    private static void addPerson(int start, int end) throws ElevatorInvalidDataException {
        Direction d = Direction.determineDirection(start, end);
        Person p = new Person("P" + personCounter++, start, end); //personcounter is so it goes p1, p2, etc
        System.out.println(getTimeStamp() + " Person " + p.getPersonId() + " created on Floor " + start + ", wants to go " + d + " to Floor " + end);
        System.out.println(getTimeStamp() + " Person " + p.getPersonId() + " presses " + d + " on Floor " + start);
        p.setStartWaitTime(System.currentTimeMillis() - initTime);
        ElevatorController.getInstance().addFloorRequest(start, d);
        Building.getInstance().addPendingPerson(p); //here add the person to all Person arraylist in building class
        Building.getInstance().addPersonOrdered(p); //to have an ordered person list for printing stats
    }


    private static void printPeopleStats() throws ElevatorInvalidDataException {
        ArrayList<Long> waitTimeList = new ArrayList<>();
        ArrayList<Long> rideTimeList = new ArrayList<>();
        HashMap<Long, String> waitListHM = new HashMap<>();
        HashMap<Long, String> rideListHM = new HashMap<>();
        double waitTime = 0;
        double rideTime = 0;


        for (Person p : Building.getInstance().getAllPeopleOrdered()) {
            waitTimeList.add(p.getWaitTime());
            rideTimeList.add(p.getRideTime());
            waitListHM.put(p.getWaitTime(), p.getPersonId());
            rideListHM.put(p.getRideTime(), p.getPersonId());
        }

        //Sort and then calculate average wait time
        Collections.sort(waitTimeList);
        Collections.sort(rideTimeList);

        for (double time : waitTimeList) {
            waitTime += time;
        }

        for (double time : rideTimeList) {
            rideTime += time;
        }

        double avgWaitTime = waitTime / waitTimeList.size();
        double avgRideTime = rideTime / rideTimeList.size();

        //print out avg, min, max wait and ride times
        System.out.println("***Simulation Complete***");
        System.out.println(String.format("%15s %.2f", "Avg Wait Time: ", avgWaitTime));
        System.out.println(String.format("%15s %.2f", "Avg Ride Time: ", avgRideTime));

        System.out.println("Min Wait Time: " + waitTimeList.get(0) + " (" + waitListHM.get(waitTimeList.get(0)) + ")");
        System.out.println("Min Ride Time: " + rideTimeList.get(0) + " (" + rideListHM.get(rideTimeList.get(0)) + ")");

        System.out.println("Max Wait Time: " + waitTimeList.get(waitTimeList.size() - 1) + " (" + waitListHM.get(waitTimeList.get(waitTimeList.size() - 1)) + ")");
        System.out.println("Max Ride Time: " + rideTimeList.get(rideTimeList.size() - 1) + " (" + rideListHM.get(rideTimeList.get(waitTimeList.size() - 1)) + ")");
        System.out.println();

        //print out Person Values
        System.out.println(String.format("%-15s %15s %15s %15s %15s %15s %15s", "Person", "Start Floor", "End Floor", "Direction", "Wait Time", "Ride Time", "Total Time"));
        System.out.println(String.format("%-15s %15s %15s %15s %15s %15s %15s", "---------", "------------", "---------", "---------", "---------", "---------", "---------"));
        for (Person p : Building.getInstance().getAllPeopleOrdered()) {
            System.out.println(String.format("%-15s %15s %15s %15s %15s %15s %15s", p.getPersonId(), p.getPersonStartFloor(), p.getDestinationFloor(), Direction.determineDirection(p.getPersonStartFloor(), p.getDestinationFloor()), p.getWaitTime(), p.getRideTime(), p.getTotalTime()));
        }
    }


    //returns the timestamp of the execution
    public static String getTimeStamp() {
        long now = System.currentTimeMillis() - initTime;

        long hours = now / 360000;
        now -= (hours * 3600000);

        long minutes = now / 60000;
        now -= (minutes * 60000);

        long seconds = now / 1000;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);

    }

    public static long getInitTime() {
        return initTime;
    }

}