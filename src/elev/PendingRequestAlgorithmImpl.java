package elev;

import exceptions.ElevatorInvalidDataException;

import java.util.ArrayList;

public class PendingRequestAlgorithmImpl implements PendingRequestAlgorithm {

    @Override
    public ArrayList<Request> findPending(int inElevatorId, ArrayList<Request> inPendingRequests) throws ElevatorInvalidDataException {
        ArrayList<Request> tmpMorePending = new ArrayList<>();
        ArrayList<Request> pendingRequests = inPendingRequests;
        ArrayList<Request> requestsToRemoveFromPending = new ArrayList<>();

        //if pending requests list isn't empty, get the first request and add it when an elevator gets idle
        if (!pendingRequests.isEmpty()) {
            Request firstRequest = pendingRequests.get(0);
            ElevatorController.getInstance().addRiderRequest(inElevatorId, firstRequest);
            System.out.println("Checking if first request works: " + firstRequest);
            pendingRequests.remove(0);
            requestsToRemoveFromPending.add(firstRequest);


            if (!pendingRequests.isEmpty()) {
                for (Request request : pendingRequests) {
                    //request DOWN
                    if (firstRequest.getDirection() == Direction.DOWN) {
                        if (request.getDirection() == firstRequest.getDirection() && request.getFloor() <= firstRequest.getFloor()) {
                            tmpMorePending.add(request);
                        }
                    }
                    //if firstRequest is UP
                    if (firstRequest.getDirection() == Direction.UP) {
                        if (request.getDirection() == firstRequest.getDirection() && request.getFloor() >= firstRequest.getFloor()) {
                            tmpMorePending.add(request);
                        }
                    }
                }
                for (Request request : tmpMorePending) {
                    System.out.println("Pending Request added to elevator new impl pending: " + request);
                    ElevatorController.getInstance().addRiderRequest(inElevatorId, request); //adds any additional requests to the idle elevator
                    requestsToRemoveFromPending.add(request); //add it to temporarily list to remove from Pending list
                }
                for (Request request : requestsToRemoveFromPending) {
                    pendingRequests.remove(request);
                }
            }
            return requestsToRemoveFromPending;

        } else {
            return null;
        }

    }
}