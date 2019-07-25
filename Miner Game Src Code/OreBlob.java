import java.util.List;
import edu.calpoly.spritely.Tile;
public class OreBlob extends MovingEntity
{
    public static final List<Tile> blobTiles;
    static{
        blobTiles = VirtualWorld.loadImages("blob", "===*===*===*");
    }
    public OreBlob(Point position, List<Tile> tiles, int actionPeriod, int animationPeriod)
    {
        super(position, tiles, actionPeriod, animationPeriod);
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

    @Override
    protected void executeActivity(WorldModel world,
                           EventSchedule eventSchedule)
    {
        Vein blobTarget = (Vein) world.findNearest(
            this.position, (Entity e) -> e instanceof Vein);
        long nextPeriod = this.actionPeriod;

        if (blobTarget != null)
        {
            Point tgtPos = blobTarget.position;

            if (this.moveTo(world, blobTarget, eventSchedule))
            {
                Quake quake = new Quake(tgtPos, Quake.quakeTiles, 1100, 100);

                world.addEntity(quake);
                nextPeriod += this.actionPeriod;
                quake.scheduleActions(eventSchedule, world);
            }
        }

        eventSchedule.scheduleEvent(this,
            this.createActivityAction(world),
            nextPeriod);
    }

}
