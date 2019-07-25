
import edu.calpoly.spritely.SpriteWindow;
/**
 * The main entry point to our program.
 **/

public class Main {

    private static void usage() {
        System.out.println("Usage:  java Main [args]\n");
        System.out.println();
        System.out.println("    -text    Force program to run in text mode");
        System.out.println("    -fast    Run simuation 2x faster");
        System.out.println("    -faster  Run simuation 4x faster");
        System.out.println("    -fastest Run simuation 10x faster");
        System.out.println("    -test    Run unit tests");
        System.out.println("    -path    Run Pathing tests");
        System.out.println("");
        System.exit(1);
    }

    private static void runTestsAndExit() {
        TestCases tests = new TestCases();
        int failed = tests.runTests();
        if (failed > 0) {
            System.exit(1);
        } else {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        VirtualWorld world = new VirtualWorld();
        for (String arg : args) {
            if ("-text".equals(arg)) {
                SpriteWindow.setTextMode();
            } else if ("-fast".equals(arg)) {
                world.timeScale = 2.0;
            } else if ("-faster".equals(arg)) {
                world.timeScale = 4.0;
            } else if ("-fastest".equals(arg)) {
                world.timeScale = 10.0;
            } else if ("-test".equals(arg)) {
                runTestsAndExit();
            } else if ("-path".equals(arg)){
                TestPathing.runTestsAndExit();
            }
            else {
                usage();
            }
        }
        world.runSimulation();
        System.exit(0);
    }
}
