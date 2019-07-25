import java.util.List;
import edu.calpoly.spritely.Tile;
public class MinerNotFull extends Miner
{
    public static final List<Tile> minerTiles;
    static{
        minerTiles = VirtualWorld.loadImages("miner", "mMmMm");
    }

    public MinerNotFull(Point position, List<Tile> tiles, int resourceLimit, int resourceCount,
                  int actionPeriod, int animationPeriod)
    {
        super(position, tiles, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }

    @Override
    protected void executeActivity(WorldModel world, EventSchedule eventSchedule)
    {
        Ore notFullTarget = (Ore) world.findNearest(this.position, (Entity e) -> e instanceof Ore);

        if (notFullTarget == null ||
            !this.moveTo(world, notFullTarget, eventSchedule) ||
            !this.transform(world, eventSchedule))
        {
            eventSchedule.scheduleEvent(this,
                this.createActivityAction(world),
                this.actionPeriod);
        }
    }

}
