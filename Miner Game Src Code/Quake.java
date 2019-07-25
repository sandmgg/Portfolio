import java.util.List;
import edu.calpoly.spritely.Tile;
public class Quake extends AnimatedEntity
{
    public static final List<Tile> quakeTiles;
    static{
        quakeTiles = VirtualWorld.loadImages("quake", "QqQqQq");
    }
    public Quake(Point position, List<Tile> tiles, int actionPeriod, int animationPeriod)
    {
        super(position, tiles, actionPeriod, animationPeriod);
    }

    @Override
    protected void scheduleActions(EventSchedule eventSchedule,
                    WorldModel world)
    {
        eventSchedule.scheduleEvent(this, this.createActivityAction(world),
            this.actionPeriod);
        eventSchedule.scheduleEvent(this, this.createAnimationAction(10),
            this.getAnimationPeriod());
    }

    @Override
    protected void executeActivity(WorldModel world,
                         EventSchedule eventSchedule)
    {
        eventSchedule.unscheduleAllEvents(this);
        world.removeEntity(this);
    }
}
