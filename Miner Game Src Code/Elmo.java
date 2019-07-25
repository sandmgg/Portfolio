
import java.util.List;
import edu.calpoly.spritely.Tile;
public class Elmo extends MovingEntity
{
    public static final List<Tile> elmoTiles;
    static{
        elmoTiles = VirtualWorld.loadImages("elmo", "ELM");
    }
    public Elmo(Point position, List<Tile> tiles, int actionPeriod, int animationPeriod)
    {
        super(position, tiles, actionPeriod, animationPeriod);
    }

    @Override
    protected void scheduleActions(EventSchedule eventSchedule,
                    WorldModel world)
    {
        eventSchedule.scheduleEvent(this, this.createActivityAction(world), this.actionPeriod);
        eventSchedule.scheduleEvent(this, this.createAnimationAction(0), this.getAnimationPeriod());
    }

    @Override
    protected void executeActivity(WorldModel world,
                           EventSchedule eventSchedule)
    {
        MinerNotFull notFullTarget = (MinerNotFull) world.findNearest(
            this.position, (Entity e) -> e instanceof MinerNotFull);
        long nextPeriod = this.actionPeriod;

        if (notFullTarget != null)
        {
            Point tgtPos = notFullTarget.position;

            if (this.moveTo(world, notFullTarget, eventSchedule))
            {
                Blacksmith blacksmith = new Blacksmith(tgtPos, Blacksmith.blacksmithTiles);

                world.addEntity(blacksmith);
                nextPeriod += this.actionPeriod;
            }
        }

        eventSchedule.scheduleEvent(this,
            this.createActivityAction(world),
            nextPeriod);
    }
}
