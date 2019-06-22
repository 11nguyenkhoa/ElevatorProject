package elev;

public class PendingRequestAlgorithmFactory {
    public static PendingRequestAlgorithm create() {
        return new PendingRequestAlgorithmImpl();
    }
}
