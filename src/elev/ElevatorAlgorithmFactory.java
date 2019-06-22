package elev;


public class ElevatorAlgorithmFactory {//dynamically picks an algorithm. Here we only have one. Here we are choosing ElevatorAlgorithmImpl to return, can have many ElevatorAlgorithmImpls here if need be

    public static ElevatorAlgorithm create() {
        return new ElevatorAlgorithmImpl();
    }
}
