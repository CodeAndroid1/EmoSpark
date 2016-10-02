package ibmmobileappbuilder.util;

/**
 * Numeric utility methods
 */
public class MathUtils {

    /**
     * Calculate the order of magnitude of a number (float by now)
     *
     * @param range the input string
     * @return the resulting order
     */
    public static int getOrderOfMagnitude(int range) {

        int aux = range;
        int mag = 1;
        while (aux > 10) {
            mag = mag * 10;
            aux = aux / 10;
        }
        ;
        return mag;
    }


}
