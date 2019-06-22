package elev;

import java.util.ArrayList;

public class ElevatorAlgorithmImpl implements ElevatorAlgorithm {


    @Override
    public Integer findBestElevator(ArrayList<ArrayList> elevators, Request requestToAdd) {
        ArrayList<Integer> tmpElevators1 = new ArrayList<>();

        //loops through all elevators to see if current Floor matches floor request; If so add to tmpElevator
        //elevList[0] is elevID, elevList[1] is current floor, elevList[2] is direction
        for (ArrayList elevList : elevators) {
            if (((int) elevList.get(1)) == requestToAdd.getFloor() && (elevList.get(2) == requestToAdd.getDirection() || elevList.get(2) == Direction.IDLE)) {
                tmpElevators1.add(((int) elevList.get(0)));
            }
            //Is there an elevator on the floor and (elevator going in right direction or the elevator is idle)
            else if (((elevList.get(2)) != Direction.IDLE && requestToAdd.getDirection() == Direction.UP)) {
                if (elevList.get(2) == requestToAdd.getDirection() && ((int) elevList.get(1)) < requestToAdd.getFloor()) {
                    tmpElevators1.add(((int) elevList.get(0)));
                }

            } else if (((elevList.get(2)) != Direction.IDLE && requestToAdd.getDirection() == Direction.DOWN)) {
                if (elevList.get(2) == requestToAdd.getDirection() && ((int) elevList.get(1)) > requestToAdd.getFloor()) {
                    tmpElevators1.add(((int) elevList.get(0)));
                }
            }
            //if there's no elevator on current floor and if there's no elevator moving in desired direction, is there an IDLE elevator?
            else if (elevList.get(2) == Direction.IDLE) {
                tmpElevators1.add(((int) elevList.get(0)));
            }
        }

        if (tmpElevators1.isEmpty() == false) {
            return ((int) tmpElevators1.get(0)); //return elevID else return null
        } else return null;

    }
}

