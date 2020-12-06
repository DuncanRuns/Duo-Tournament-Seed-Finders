package ca.duncansserver.duoseedfinder;

public class FoundSeed {
    public final int x;
    public final int z;
    public final Long seed;

    public FoundSeed(Long seed, int x, int z) {
        this.x = x;
        this.z = z;
        this.seed = seed;
    }

    public FoundSeed(Long seed) {
        this.x = 0;
        this.z = 0;
        this.seed = seed;
    }

    public String toString() {
        return seed + " /tp @s " + x + " ~ " + z;
    }

}
