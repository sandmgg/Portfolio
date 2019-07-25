import java.util.List;
import edu.calpoly.spritely.Tile;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.function.Function;
import java.util.function.ToIntBiFunction;

public abstract class MovingEntity extends AnimatedEntity
{
    protected static PathingStrategy path;

    static{
        path = new AStarPathingStrategy();
    }
    public MovingEntity(Point position, List<Tile> tiles, int actionPeriod, int animationPeriod)
    {
        super(position, tiles, actionPeriod, animationPeriod);
    }

    protected boolean blobChecker(Entity entity)
    {
        if (entity instanceof OreBlob)
        {
            return true;
        }
        return false;
    }

    protected boolean fullChecker(Entity entity)
    {
        if (entity instanceof MinerFull)
        {
            return true;
        }
        return false;
    }

    protected boolean notFullChecker(Entity entity)
    {
        if (entity instanceof MinerNotFull)
        {
            return true;
        }
        return false;
    }

    private Predicate<Point> setupCanPassThrough(WorldModel world) {
        return (Point p) -> {
            if (p.getX() < 0 || p.getX() >= VirtualWorld.WORLD_SIZE.width) {
                return false;
            }
            if (p.getY() < 0 || p.getY() >= VirtualWorld.WORLD_SIZE.height) {
                return false;
            }
            boolean isBlob = blobChecker(this);
            if (isBlob){
                if (world.isOccupied(p)) {
                    if (!(world.getOccupant(p) instanceof Ore)) {
                        return false;
                    }
                }

            }
            else{
                if (world.isOccupied(p)) {
                    return false;
                }
            }
            return true;
        };
    }

    private Function<Point, List<Point>> setupPotentialNeighbors() {
        return (Point p) -> {
            if (this instanceof Elmo)
            {
                ArrayList<Point> result = new ArrayList<>(6);
                result.add(new Point(p.getX()+1, p.getY()+1));
                result.add(new Point(p.getX()+1, p.getY()-1));
                result.add(new Point(p.getX()-1, p.getY()-1));
                result.add(new Point(p.getX()-1, p.getY()+1));
                result.add(new Point(p.getX()-1, p.getY()));
                result.add(new Point(p.getX()+1, p.getY()));
                return result;
            }
            else {
                ArrayList<Point> result = new ArrayList<>(4);
                result.add(new Point(p.getX()-1, p.getY()));
                result.add(new Point(p.getX()+1, p.getY()));
                result.add(new Point(p.getX(), p.getY()-1));
                result.add(new Point(p.getX(), p.getY()+1));
                return result;
            }
        };
    }

    private ToIntBiFunction<Point, Point> setupStepsFromTo() {
        return (Point p1, Point p2)
                -> Math.abs(p1.getX() - p2.getX())
                   + Math.abs(p1.getY() - p2.getY());
    }

    protected Point nextPosition(WorldModel world, Point destPos)
    {

        Predicate<Point> canPassThrough = setupCanPassThrough(world);
        Function<Point, List<Point>> potentialNeighbors = setupPotentialNeighbors();
        ToIntBiFunction<Point, Point> stepsFromTo = setupStepsFromTo();
        List<Point> moveOptions = path.computePath(this.position, destPos, canPassThrough, potentialNeighbors, stepsFromTo);
        if (moveOptions.size() > 0)
        {
            return moveOptions.get(0);
        }
        return this.position;

    }
    protected boolean moveTo(WorldModel world,
               Entity target,  EventSchedule eventSchedule)
    {
        if (blobChecker(this))
        {
            if (this.position.adjacent(target.position))
            {
                world.removeEntity(target);
                eventSchedule.unscheduleAllEvents(target);
                return true;
            }
            else
            {
                Point nextPos = this.nextPosition(world, target.position);

                if (!this.position.equals(nextPos))
                {
                    Entity occupant = world.getOccupant(nextPos);
                    if (occupant != null)
                    {
                        eventSchedule.unscheduleAllEvents(occupant);
                    }

                    world.moveEntity(this, nextPos);
                }
                return false;
            }
        }
        else if (fullChecker(this)) {
            if (this.position.adjacent(target.position))
            {
                return true;
            }
            else
            {
                Point nextPos = this.nextPosition(world, target.position);

                if (!this.position.equals(nextPos))
                {
                    world.moveEntity(this, nextPos);
                }
                return false;
            }
        }
        else if (notFullChecker(this)) {
            if (this.position.adjacent(target.position))
            {
            ((MinerNotFull) this).resourceCount += 1;
            world.removeEntity(target);
            eventSchedule.unscheduleAllEvents(target);

            return true;
            }
            else
            {
                Point nextPos = this.nextPosition(world, target.position);

                if (!this.position.equals(nextPos))
                {
                    world.moveEntity(this, nextPos);
                }
                return false;
            }
        }
        else {
            if (this.position.adjacent(target.position))
            {
            world.removeEntity(target);
            eventSchedule.unscheduleAllEvents(target);
            return true;
            }
            else
            {
                Point nextPos = this.nextPosition(world, target.position);

                if (!this.position.equals(nextPos))
                {
                    world.moveEntity(this, nextPos);
                }
                return false;
            }
        }
    }

}
