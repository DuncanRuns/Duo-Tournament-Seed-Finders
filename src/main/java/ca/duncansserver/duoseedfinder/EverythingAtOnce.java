package ca.duncansserver.duoseedfinder;

public class EverythingAtOnce {

    public static void main(String[] args) {

        System.out.println("Starting Darcoke Finder...");
        DarkForestFinder.main(args);
        System.out.println("Darcoke Done, Starting Igloo Finder...");
        IglooFinder.main(args);
        System.out.println("Igloo Done, Starting Temple/Savillage Finder...");
        SavannaVillageTempleFinder.main(args);
        System.out.println("Everything Done! :D");

    }

}
