package ca.duncansserver.duoseedfinder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.time.Clock;

import kaptainwutax.biomeutils.Biome;
import kaptainwutax.biomeutils.source.BiomeSource;
import kaptainwutax.biomeutils.source.OverworldBiomeSource;
import kaptainwutax.featureutils.structure.Fortress;
import kaptainwutax.featureutils.structure.Igloo;
import kaptainwutax.featureutils.structure.RegionStructure;
import kaptainwutax.featureutils.structure.Village;
import kaptainwutax.seedutils.mc.ChunkRand;
import kaptainwutax.seedutils.mc.MCVersion;

public class IglooSeedFinder {

    public static final Village village = new Village(MCVersion.v1_14_4);
    public static final Fortress fortress = new Fortress(MCVersion.v1_14_4);
    public static final Igloo igloo = new Igloo(MCVersion.v1_14_4);
    public static final ChunkRand rand = new ChunkRand();

    public static void main(String[] args) throws InterruptedException {

        Long startSeed = 0L;
        int numFound = 0;
        final int toFind = 1000;
        Queue<FoundSeed> foundSeeds = new LinkedList<>();

        seedloop: for (Long worldSeed = startSeed; worldSeed < 1L << 48; worldSeed++) {

            if (!RandomChecker.isRandom(worldSeed)) {

                // Fortress Check
                @SuppressWarnings("unchecked")
                RegionStructure.Data<Fortress> fortressData = (RegionStructure.Data<Fortress>) fortress.at(4, 4);
                boolean fortressFound = fortress.canStart(fortressData, worldSeed, rand);
                if (!fortressFound) {
                    continue;
                }

                // Extra Setup
                BiomeSource seedBiomeSource = new OverworldBiomeSource(MCVersion.v1_14_4, worldSeed);

                // Village Check
                int villageX = 0;
                int villageZ = 0;

                boolean villageFound = false;

                outerloop: for (int x = 0; x < 8; x++) {
                    for (int z = 0; z < 8; z++) {
                        // x and z are chunk coordinates
                        RegionStructure.Data<?> villageData = village.at(x, z);
                        boolean possibleVillage = villageData.testStart(worldSeed, rand);
                        if (possibleVillage) {
                            Biome biomeAtPV = seedBiomeSource.getBiomeForNoiseGen(x * 4, 0, z * 4);

                            if (biomeAtPV == Biome.SNOWY_TUNDRA) {
                                villageFound = true;
                                villageX = x;
                                villageZ = z;
                                break outerloop;
                            }
                        }
                    }
                }

                if (!villageFound) {
                    continue;
                }

                // Igloo Check
                boolean foundIgloo = false;

                outerloop: for (int x = -3; x < 4; x++) {
                    for (int z = -3; z < 4; z++) {
                        int ix = x + villageX;
                        int iz = z + villageZ;

                        RegionStructure.Data<?> iglooData = igloo.at(ix, iz);
                        boolean possibleIgloo = iglooData.testStart(worldSeed, rand);

                        if (possibleIgloo) {
                            if (iglooData.testBiome(seedBiomeSource)) {
                                foundIgloo = true;
                                break outerloop;
                            }
                        }

                    }
                }

                if (!foundIgloo) {
                    continue;
                }

                FoundSeed thisFoundSeed = new FoundSeed(worldSeed, villageX * 16, villageZ * 16);
                System.out.println(thisFoundSeed);
                foundSeeds.add(thisFoundSeed);
                numFound++;

                if (numFound % 50 == 0) {
                    System.out.println(100.0 * ((float) numFound / toFind) + "%");
                }

                if (numFound == toFind) {
                    File outputFile = new File("Tundra Village + Igloo " + Clock.systemUTC().millis() + ".txt");
                    try {
                        FileWriter outputWriter = new FileWriter(outputFile);
                        for (FoundSeed foundSeed : foundSeeds) {
                            outputWriter.write(foundSeed.toString() + "\n");
                        }
                        outputWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break seedloop;
                }

            }

        }

    }
}
