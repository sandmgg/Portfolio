import java.util.List;
import edu.calpoly.spritely.Tile;
public class Ore extends ActiveEntity
{
    public static final List<Tile> oreTiles;
    static{
        oreTiles = VirtualWorld.loadImages("ore", "$");
    }
    public Ore(Point position, List<Tile> tiles, int actionPeriod)
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
        Point pos = this.position;    // store current position before removing

        world.removeEntity(this);
        eventSchedule.unscheduleAllEvents(this);

        OreBlob blob = new OreBlob(pos, OreBlob.blobTiles, this.actionPeriod / 4,
                                    50 + rand.nextInt(100));

        world.addEntity(blob);
        blob.scheduleActions(eventSchedule, world);
    }

}
