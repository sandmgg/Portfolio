import java.util.List;
import edu.calpoly.spritely.Tile;
public class Vein extends ActiveEntity
{
    public static final List<Tile> veinTiles;
    static{
        veinTiles = VirtualWorld.loadImages("vein", "V");
    }
    public Vein(Point position, List<Tile> tiles, int actionPeriod)
    {
        super(position, tiles, actionPeriod);
    }

    @Override
    protected void scheduleActions(EventSchedule eventSchedule, WorldModel world)
    {
        eventSchedule.scheduleEvent(this,
            this.createActivityAction(world),
            this.actionPeriod);
    }

    @Override
    protected void executeActivity(WorldModel world,
                        EventSchedule eventSchedule)
    {
        Point openPt = world.findOpenAround(this.position);

        if (openPt != null) {
            Ore ore = new Ore(openPt, Ore.oreTiles, 20000 + rand.nextInt(10000));
            world.addEntity(ore);
            ore.scheduleActions(eventSchedule, world);
        }

        eventSchedule.scheduleEvent(this,
            this.createActivityAction(world),
            this.actionPeriod);
    }
}
