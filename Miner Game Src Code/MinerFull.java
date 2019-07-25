import java.util.List;
import edu.calpoly.spritely.Tile;
public class MinerFull extends Miner
{
    public static final List<Tile> minerFullTiles;
    static{
        minerFullTiles = VirtualWorld.loadImages("miner_full", "mM$mM");
    }
    public MinerFull(Point position, List<Tile> tiles, int resourceLimit, int resourceCount,
                  int actionPeriod, int animationPeriod)
    {
        super(position, tiles, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }

    @Override
    protected void executeActivity(WorldModel world,
                             EventSchedule eventSchedule)
    {
        Blacksmith fullTarget
            = (Blacksmith) world.findNearest(this.position, (Entity e) -> e instanceof Blacksmith);


        if (fullTarget != null  &&
            this.moveTo(world, fullTarget, eventSchedule))
        {
            this.transform(world, eventSchedule);
        }
        else
        {
            eventSchedule.scheduleEvent(this,
                this.createActivityAction(world),
                this.actionPeriod);
        }
    }

}
