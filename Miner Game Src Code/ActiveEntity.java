import java.util.List;
import edu.calpoly.spritely.Tile;

public abstract class ActiveEntity extends Entity
{
    protected final int actionPeriod;

    public ActiveEntity(Point position, List<Tile> tiles, int actionPeriod)
    {
        super(position, tiles);
        this.actionPeriod = actionPeriod;
    }


    protected abstract void scheduleActions(EventSchedule eventSchedule, WorldModel world);

    protected abstract void executeActivity(WorldModel world, EventSchedule eventSchedule);

    protected Action createActivityAction(WorldModel world)
    {
        return new Activity(this, world, 0);
    }
}
