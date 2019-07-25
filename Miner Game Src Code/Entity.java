import java.util.List;
import edu.calpoly.spritely.Tile;
import java.util.Random;

/**
 * An entity in our virtual world.  An entity occupies a square
 * on the grid.  It might move around, and interact with other
 * entities in the world.
 */
public abstract class Entity
{
    protected Point position;
    protected final List<Tile> tiles;
    protected int tileIndex;       // Index into tiles for animation
    protected static final Random rand = new Random();


    public Entity(Point position, List<Tile> tiles)
    {
        this.position = position;
        this.tiles = tiles;
        this.tileIndex = 0;

    }

    protected Point getPosition()
    {
        return this.position;
    }

    protected void setPosition(Point position)
    {
        this.position = position;
    }

    protected Tile getCurrentTile()
    {
        return this.tiles.get(this.tileIndex);
    }

}
