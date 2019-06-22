package elev;

import exceptions.ElevatorInvalidDataException;

import java.util.ArrayList;

public interface PendingRequestAlgorithm {
    ArrayList<Request> findPending(int inElevatorId, ArrayList<Request> inPendingRequests) throws ElevatorInvalidDataException;

}
