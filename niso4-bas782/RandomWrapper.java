import java.util.Random;

public class RandomWrapper {
    private static Random rnd;

    private RandomWrapper() {
    }

    public static synchronized Random getRandom() {
        if (rnd == null) {
            rnd = new Random();
        }
        return rnd;
    }
}
