import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import edu.calpoly.spritely.Size;
import edu.calpoly.spritely.Tile;
import edu.calpoly.spritely.AnimationFrame;
import java.util.List;
import java.util.LinkedList;
import java.awt.Color;
import edu.calpoly.spritely.SolidColorTile;
import edu.calpoly.spritely.Tile;
import java.util.function.Predicate;
import java.util.ArrayList;

/**
 * Data structures that hold the model of our virtual world.
 * It consists of a grid.  Each point on the grid is occupied
 * by a background tile, and, optionally, an Entity.
 */
final class WorldModel
{
    private final Size size;
    public final Tile background[][];
    private final Entity occupant[][];
    public final Set<Entity> entities;
    public Alpha alpha;

    public WorldModel(Size gridSize)
    {
        this.size = gridSize;
        this.background = new Tile[gridSize.height][gridSize.width];
        this.occupant = new Entity[gridSize.height][gridSize.width];
        this.entities = new HashSet<Entity>();
        this.alpha = new Alpha();
    }

    /**
     * Paint the WorldModel to frame, by adding tiles to frame's
     * grid.
     */
    public void paint(AnimationFrame frame) {
        //Tile grass = VirtualWorld.getImageTile("grass.png", '.');
        for (int y = 0; y < this.size.height; y++) {
            for (int x = 0; x < this.size.width; x++) {
                frame.addTile(x, y, this.background[y][x]);
                if (this.background[y][x] == VirtualWorld.grass) {
                frame.addTile(x,y, new SolidColorTile(new Color(100, 255, 0, (int)(75 *alpha.returnAlpha())), 's'));
                    }
                Entity occupant = this.occupant[y][x];
                if (occupant != null) {
                    Tile tile = occupant.getCurrentTile();
                    frame.addTile(x, y, tile);
                }
            }
        }
    }

    /*
        Assumes that there is no entity currently occupying the
        intended destination cell.
    */
    public void addEntity(Entity entity)
    {
        if (this.withinBounds(entity.getPosition()))
        {
            this.setOccupantCell(entity.getPosition(), entity);
            this.entities.add(entity);
        }
    }

    public Entity getOccupant(Point pos)
    {
        if (this.isOccupied(pos)) {
            return getOccupantCell(pos);
        } else {
            return null;
        }
    }

    private Entity getOccupantCell(Point pos)
    {
        return this.occupant[pos.getY()][pos.getX()];
    }

    public boolean isOccupied(Point pos)
    {
        return this.withinBounds(pos) && this.getOccupantCell(pos) != null;
    }

    public void removeEntity(Entity entity)
    {
        this.removeEntityAt(entity.getPosition());
    }

    private void removeEntityAt(Point pos)
    {
        if (this.withinBounds(pos)
            && this.getOccupantCell(pos) != null)
        {
            Entity entity = this.getOccupantCell(pos);

            /* this moves the entity just outside of the grid for
                debugging purposes */
            entity.setPosition(new Point(-1, -1));
            this.entities.remove(entity);
            this.setOccupantCell(pos, null);
        }
    }

    private void setOccupantCell(Point pos, Entity entity)
    {
        this.occupant[pos.getY()][pos.getX()] = entity;
    }

    private boolean withinBounds(Point pos)
    {
        return pos.getY() >= 0 && pos.getY() < this.size.height &&
            pos.getX() >= 0 && pos.getX() < this.size.width;
    }

    private Entity nearestEntity(List<Entity> entities, Point pos)
    {
        if (entities.isEmpty()) {
            return null;
        } else {
            Entity nearest = entities.get(0);
            int nearestDistance = nearest.getPosition().distanceSquared(pos);

            for (Entity other : entities)
            {
                int otherDistance = other.getPosition().distanceSquared(pos);

                if (otherDistance < nearestDistance)
                {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return nearest;
        }
    }

    public Entity findNearest(Point pos, Predicate<Entity> isTarget)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : this.entities)
        {
            if (isTarget.test(entity) == true)
            {
                ofType.add(entity);
            }
        }

        return nearestEntity(ofType, pos);

    }

    public Point findOpenAround(Point pos)
    {
        for (int dy = -1; dy <= 1; dy++)
        {
            for (int dx = -1; dx <= 1; dx++)
            {
                Point newPt = new Point(pos.getX() + dx, pos.getY() + dy);
                if (this.withinBounds(newPt) &&
                    !this.isOccupied(newPt))
                {
                    return newPt;
                }
            }
        }

        return null;
    }

    public void moveEntity(Entity entity, Point pos)
    {
        Point oldPos = entity.getPosition();
        if (this.withinBounds(pos) && !pos.equals(oldPos))
        {
            this.setOccupantCell(oldPos, null);
            this.removeEntityAt(pos);
            this.setOccupantCell(pos, entity);
            entity.setPosition(pos);
        }
    }

    public List<Point> setupNeighbors(Point p) {
        ArrayList<Point> potentialNeighbors = new ArrayList<>(9);
        potentialNeighbors.add(new Point(p.getX()+1, p.getY()+1));
        potentialNeighbors.add(new Point(p.getX()+1, p.getY()-1));
        potentialNeighbors.add(new Point(p.getX()-1, p.getY()-1));
        potentialNeighbors.add(new Point(p.getX()-1, p.getY()+1));
        potentialNeighbors.add(new Point(p.getX()-1, p.getY()));
        potentialNeighbors.add(new Point(p.getX()+1, p.getY()));
        potentialNeighbors.add(new Point(p.getX(), p.getY()-1));
        potentialNeighbors.add(new Point(p.getX(), p.getY()+1));
        potentialNeighbors.add(new Point(p.getX(), p.getY()));
        List<Point> freezeSquares = new ArrayList<Point>();
        for (Point po: potentialNeighbors)
        {
            if (withinBounds(po))
            {
                freezeSquares.add(po);
            }
        }
        return freezeSquares;

    }

}
