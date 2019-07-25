

import edu.calpoly.testy.Testy;
import java.util.List;
import java.util.ArrayList;
import static edu.calpoly.testy.Assert.assertEquals;
import static edu.calpoly.testy.Assert.assertNotNull;


/**
 * This class contains unit tests for Minecraft 2: Electric Boogaloo.
 */
public class TestCases {

    private void loadImagesTest() {
        // This will fail if an image name is misspelled.  By doing this
        // here, we make checkgit test image loading.
        //VirtualWorld.loadEntityImages();
        VirtualWorld testWorld= new VirtualWorld();
        assertNotNull(Blacksmith.blacksmithTiles);
        assertNotNull(OreBlob.blobTiles);
        assertNotNull(MinerNotFull.minerTiles);
        assertNotNull(MinerFull.minerFullTiles);
        assertNotNull(Obstacle.obstacleTiles);
        assertNotNull(Ore.oreTiles);
        assertNotNull(Quake.quakeTiles);
        assertNotNull(Vein.veinTiles);

    }
    private void  dummyAnimationTest()
    {
        DummyAnimation a = new DummyAnimation();

        EventSchedule eventSchedule = new EventSchedule();
        eventSchedule.scheduleEvent(a, new Animation(a,  30), 1);
        eventSchedule.processEvents(100);

        assertEquals(30, a.count);

    }
    private void  dummyAnimationTest2()
    {
        DummyAnimation a = new DummyAnimation();

        EventSchedule eventSchedule = new EventSchedule();
        eventSchedule.scheduleEvent(a, new Animation(a, 0), 1);
        eventSchedule.processEvents(100);
        assertEquals(100.0, eventSchedule.getCurrentTime());
        assertEquals(100, a.count);

    }

    /**
     * Run the tests.
     *
     * @return The number of failures.
     */
    public int runTests() {
        return Testy.run(
                () -> loadImagesTest(),
                () -> dummyAnimationTest(),
                () -> dummyAnimationTest2()
        );
    }
}
