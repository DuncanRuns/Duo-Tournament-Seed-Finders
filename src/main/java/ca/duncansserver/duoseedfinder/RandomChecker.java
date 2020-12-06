package ca.duncansserver.duoseedfinder;

import kaptainwutax.mathutils.util.Mth;
import kaptainwutax.seedutils.lcg.rand.JRand;

public class RandomChecker {
    public static boolean isRandom(long worldSeed) {
        long upperBits = worldSeed >>> 32;
        long lowerBits = worldSeed & Mth.MASK_32;

        long a = (24667315L * upperBits + 18218081L * lowerBits + 67552711L) >> 32;
        long b = (-4824621L * upperBits + 7847617L * lowerBits + 7847617L) >> 32;
        long seed = 7847617L * a - 18218081L * b;

        return JRand.nextLong(seed) == worldSeed;
    }
}
