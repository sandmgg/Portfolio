import java.util.List;
import edu.calpoly.spritely.Tile;
public abstract class Miner extends MovingEntity
{
    protected final int resourceLimit;
    protected int resourceCount;
    public Miner(Point position, List<Tile> tiles, int resourceLimit, int resourceCount,
                  int actionPeriod, int animationPeriod)
    {
        super(position, tiles, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }

    @Override
    protected void scheduleActions(EventSchedule eventSchedule,
                    WorldModel world)
    {
        eventSchedule.scheduleEvent(this, this.createActivityAction(world),
            this.actionPeriod);
        eventSchedule.scheduleEvent(this,
            this.createAnimationAction(0), this.getAnimationPeriod());
    }

    protected boolean fullChecker(Entity entity)
    {
        if (entity instanceof MinerFull)
        {
            return true;
        }
        return false;
    }

    protected boolean transform(WorldModel world, EventSchedule eventSchedule)
    {
        if (fullChecker(this)) {
            MinerNotFull miner = new MinerNotFull(this.position, MinerNotFull.minerTiles, this.resourceLimit, 0, this.actionPeriod, this.animationPeriod);//this.createMinerNotFull(this.resourceLimit,
                //this.position, this.actionPeriod, this.animationPeriod);

            world.removeEntity(this);
            eventSchedule.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(eventSchedule, world);
            return true;
        }
        else {
            if (this.resourceCount >= this.resourceLimit)
            {
                MinerFull miner = new MinerFull(this.position, MinerFull.minerFullTiles, this.resourceLimit, this.resourceLimit, this.actionPeriod, this.animationPeriod);

                world.removeEntity(this);
                eventSchedule.unscheduleAllEvents(this);

                world.addEntity(miner);
                miner.scheduleActions(eventSchedule, world);

                return true;
            }

            return false;
        }
    }
}
