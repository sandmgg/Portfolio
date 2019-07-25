
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import edu.calpoly.spritely.AnimationFrame;
import edu.calpoly.spritely.ImageTile;
import edu.calpoly.spritely.Size;
import edu.calpoly.spritely.SolidColorTile;
import edu.calpoly.spritely.SpriteWindow;
import edu.calpoly.spritely.Tile;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A representation of a virtual world, containing various entities
 * that move around a grid.  The data structures representing the
 * current state of the virtual world are split out in a separate
 * model class, called WorldModel.
 */
public final class VirtualWorld
{
    public static final Size TILE_SIZE;
    public static final Size WORLD_SIZE;
    // Name, as decided by CSC 203 in Spring 2018:
    public static final String NAME;
    public static final File IMAGE_DIR;

    public static final String[] BACKGROUND;
    private WorldModel model;
    private EventSchedule eventSchedule;
    private SpriteWindow window;
    public double timeScale;
    public static Tile grass;
    public static Tile rocks;
    public static Tile ice;
    static {
        TILE_SIZE = new Size(32, 32);
        WORLD_SIZE = new Size(40, 30);
        NAME = "Minecraft 2: Electric Boogaloo";
        IMAGE_DIR = new File("images");
        BACKGROUND = new String[] {
            "                   R                    ",
            "                    R                  R",
            " RR   RR   RR                           ",
            "R  R R  R R  R                          ",
            "   R R  R    R                          ",
            " RR  R  R  RR                           ",
            "R    R  R    R                          ",
            "R    R  R R  R                          ",
            "RRRR  RR   RR                           ",
            "                                        ",
            "                                        ",
            "                                        ",
            "                                        ",
            "                    R                  R",
            "                   R                    ",
            "                    R                  R",
            "                   R                    ",
            "                                        ",
            "                                        ",
            "                                        ",
            "                                        ",
            "                                        ",
            "                                        ",
            "                                        ",
            "                                        ",
            "                                        ",
            "                                        ",
            "                                        ",
            "                   R                    ",
            "                    R                   "
        };

        grass = getImageTile("grass.png", '.');
        rocks = getImageTile("rocks.png", '=');
        ice = getImageTile("freeze4.png", 'i');


    }
    public VirtualWorld()
    {
        model = new WorldModel(WORLD_SIZE);
        eventSchedule = new EventSchedule();
        window = new SpriteWindow(NAME, WORLD_SIZE);
        timeScale = 1.0;
    }

    public static Tile getImageTile(String imageFileName, char text) {
        Tile t = null;
        File f = new File(IMAGE_DIR, imageFileName);
        try {
            t = new ImageTile(f, TILE_SIZE, text);
        } catch (IOException ex) {
            System.out.println("Fatal error:  Image not found in " + f);
            ex.printStackTrace();
            System.exit(1);
        }
        return t;
    }

    private void setup()
    {

        window.setFps(30f);
        window.setTileSize(TILE_SIZE);
        System.out.println(NAME + ".  Press 'q' to quit.");
        window.setKeyTypedHandler((char ch) -> {
            if (ch == 'q' || ch == 'Q') {
                window.stop();
            }
        });
        setFreezeClick();

        setupBackground();
        eventSchedule.scheduleEvent(null, new Animation(model.alpha, 0), 0);
        //loadEntityImages();
        createInitialEntities();
        scheduleInitialActions(model, eventSchedule);
    }

    private void setupBackground() {

        for (int y = 0; y < WORLD_SIZE.height; y++) {
            for (int x = 0; x < WORLD_SIZE.width; x++) {
                char c = BACKGROUND[y].charAt(x);
                if (c == ' ') {
                    model.background[y][x] = grass;
                } else if (c == 'R') {
                    model.background[y][x] = rocks;
                }
                else {
                    assert false;
                }
            }
        }
    }
    private void setFreezeClick()
    {
        window.setMouseClickedHandler((int x, int y) -> {
            Elmo elmo = new Elmo(new Point(x,y), Elmo.elmoTiles, 700, 700);
            Entity toReplace = model.getOccupant(new Point(x,y));
            if (toReplace != null)
            {
                model.removeEntity(toReplace);
                eventSchedule.unscheduleAllEvents(toReplace);
            }

            model.addEntity(elmo);
            elmo.scheduleActions(eventSchedule, model);
            freezeBackground(new Point(x,y));
        });
    }
    private void freezeBackground(Point point)
    {
        //AnimationFrame nextFrame = window.waitForNextFrame();
        List<Point> neighbors = model.setupNeighbors(point);
        for (Point p: neighbors)
        {
            model.background[p.getY()][p.getX()] = ice;
            Entity holder = model.getOccupant(p);
            if (holder instanceof Miner)
            {
                model.removeEntity(holder);
                eventSchedule.unscheduleAllEvents(holder);
                FrozenMiner fm = new FrozenMiner(p, FrozenMiner.frozenMinerTiles);
                model.addEntity(fm);


            }
        }
    }



    private void createInitialEntities() {
        addInitial(new Blacksmith(new Point(0, 11), Blacksmith.blacksmithTiles));
        addInitial(new Blacksmith(new Point(0, 29), Blacksmith.blacksmithTiles));
        addInitial(new Blacksmith(new Point(19, 14), Blacksmith.blacksmithTiles));
        addInitial(new Blacksmith(new Point(19, 29), Blacksmith.blacksmithTiles));
        addInitial(new Blacksmith(new Point(20, 0), Blacksmith.blacksmithTiles));
        addInitial(new Blacksmith(new Point(39, 0), Blacksmith.blacksmithTiles));
        addInitial(new Blacksmith(new Point(39, 14), Blacksmith.blacksmithTiles));
        addInitial(new Blacksmith(new Point(39, 29), Blacksmith.blacksmithTiles));
        addInitial(new MinerNotFull(new Point(12,23), MinerNotFull.minerTiles, 2, 0, 954, 300));
        addInitial(new MinerNotFull(new Point(17,22), MinerNotFull.minerTiles, 2, 0, 982, 310));
        addInitial(new MinerNotFull(new Point(23,6), MinerNotFull.minerTiles, 2, 0, 777, 320));
        addInitial(new MinerNotFull(new Point(24,26), MinerNotFull.minerTiles, 2, 0, 851, 90));
        addInitial(new MinerNotFull(new Point(31,15), MinerNotFull.minerTiles, 2, 0, 933, 95));
        addInitial(new MinerNotFull(new Point(31,26), MinerNotFull.minerTiles, 2, 0, 734, 87));
        addInitial(new MinerNotFull(new Point(37,10), MinerNotFull.minerTiles, 2, 0, 400, 33));
        addInitial(new MinerNotFull(new Point(37,18), MinerNotFull.minerTiles, 2, 0, 888, 100));
        addInitial(new MinerNotFull(new Point(37,6), MinerNotFull.minerTiles, 2, 0, 991, 317));
        addInitial(new MinerNotFull(new Point(5,6), MinerNotFull.minerTiles, 2, 0, 992, 318));
        addInitial(new MinerNotFull(new Point(6,25), MinerNotFull.minerTiles, 2, 0, 930, 106));
        addInitial(new MinerNotFull(new Point(6,3), MinerNotFull.minerTiles, 2, 0, 813, 92));
        addInitial(new MinerNotFull(new Point(7,13), MinerNotFull.minerTiles, 2, 0, 913, 97));
        addInitial(new Obstacle(new Point(10, 23), Obstacle.obstacleTiles));
        addInitial(new Obstacle(new Point(10, 24), Obstacle.obstacleTiles));
        addInitial(new Obstacle(new Point(11, 21), Obstacle.obstacleTiles));
        addInitial(new Obstacle(new Point(11, 24), Obstacle.obstacleTiles));
        addInitial(new Obstacle(new Point(11, 25), Obstacle.obstacleTiles));
        addInitial(new Obstacle(new Point(12, 22), Obstacle.obstacleTiles));
        addInitial(new Obstacle(new Point(12, 25), Obstacle.obstacleTiles));
        addInitial(new Obstacle(new Point(12, 26), Obstacle.obstacleTiles));
        addInitial(new Obstacle(new Point(13, 22), Obstacle.obstacleTiles));
        addInitial(new Obstacle(new Point(13, 26), Obstacle.obstacleTiles));
        addInitial(new Obstacle(new Point(14, 23), Obstacle.obstacleTiles));
        addInitial(new Obstacle(new Point(14, 24), Obstacle.obstacleTiles));
        addInitial(new Obstacle(new Point(26, 26), Obstacle.obstacleTiles));
        addInitial(new Obstacle(new Point(27, 25), Obstacle.obstacleTiles));
        addInitial(new Obstacle(new Point(28, 19), Obstacle.obstacleTiles));
        addInitial(new Obstacle(new Point(28, 25), Obstacle.obstacleTiles));
        addInitial(new Obstacle(new Point(29, 20), Obstacle.obstacleTiles));
        addInitial(new Obstacle(new Point(29, 26), Obstacle.obstacleTiles));
        addInitial(new Obstacle(new Point(30, 21), Obstacle.obstacleTiles));
        addInitial(new Obstacle(new Point(31, 22), Obstacle.obstacleTiles));
        addInitial(new Obstacle(new Point(32, 23), Obstacle.obstacleTiles));
        addInitial(new Obstacle(new Point(5, 20), Obstacle.obstacleTiles));
        addInitial(new Obstacle(new Point(5, 21), Obstacle.obstacleTiles));
        addInitial(new Obstacle(new Point(6, 20), Obstacle.obstacleTiles));
        addInitial(new Obstacle(new Point(6, 21), Obstacle.obstacleTiles));
        addInitial(new Obstacle(new Point(7, 20), Obstacle.obstacleTiles));
        addInitial(new Obstacle(new Point(7, 21), Obstacle.obstacleTiles));
        addInitial(new Obstacle(new Point(8, 21), Obstacle.obstacleTiles));
        addInitial(new Obstacle(new Point(8, 22), Obstacle.obstacleTiles));
        addInitial(new Obstacle(new Point(9, 22), Obstacle.obstacleTiles));
        addInitial(new Obstacle(new Point(9, 23), Obstacle.obstacleTiles));
        addInitial(new Vein(new Point(10, 25), Vein.veinTiles, 8366));
        addInitial(new Vein(new Point(14, 22), Vein.veinTiles, 8248));
        addInitial(new Vein(new Point(21, 20), Vein.veinTiles, 9294));
        addInitial(new Vein(new Point(27, 6),  Vein.veinTiles, 9456));
        addInitial(new Vein(new Point(28, 23),  Vein.veinTiles, 13422));
        addInitial(new Vein(new Point(33, 11),  Vein.veinTiles, 10278));
        addInitial(new Vein(new Point(33, 13),  Vein.veinTiles, 10865));
        addInitial(new Vein(new Point(33, 3),  Vein.veinTiles, 11101));
        addInitial(new Vein(new Point(34, 19),  Vein.veinTiles, 11702));
        addInitial(new Vein(new Point(6, 11),  Vein.veinTiles, 15026));
        addInitial(new Vein(new Point(7, 11),  Vein.veinTiles, 9377));
        addInitial(new Vein(new Point(8, 11),  Vein.veinTiles, 13146));
    }

    private void addInitial(Entity entity) {
        assert !model.isOccupied(entity.getPosition());
        model.addEntity(entity);
    }

    /**
     * Load a list of images for an entity.  text gives a series of
     * characters that serve as the animation for the text
     * representation of the entity when in text mode.
     */
    public static List<Tile> loadImages(String fileBasename, String text) {
        int len = text.length();
        List<Tile> result = new ArrayList<Tile>(len);
        if (len == 1) {
            result.add(getImageTile(fileBasename + ".png", text.charAt(0)));
        } else {
            for (int i = 1; i <= len; i++) {
                String name = fileBasename + i + ".png";
                result.add(getImageTile(name, text.charAt(i - 1)));
            }
        }
        result = Collections.unmodifiableList(result);
        return result;
    }

    private static void scheduleInitialActions(WorldModel model, EventSchedule eventSchedule)
    {
        for (Entity entity : model.entities)
        {
            if (entity instanceof ActiveEntity)
            {
                ActiveEntity actEnt = (ActiveEntity) entity;
                actEnt.scheduleActions(eventSchedule, model);
            }
        }
    }

    /**
     * Entry point to run the virtual world simulation.
     */
    public void runSimulation() {
        setup();
        model.paint(window.getInitialFrame());
        window.start();
        while (true) {
            AnimationFrame frame = window.waitForNextFrame();
            if (frame == null) {
                break;
            }
            eventSchedule.processEvents(window.getTimeSinceStart() * timeScale);
            model.paint(frame);
            window.showNextFrame();
        }
    }

}
