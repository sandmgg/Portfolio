import java.util.List;
import edu.calpoly.spritely.Tile;
public class Obstacle extends Entity
{
    public static final List<Tile> obstacleTiles;
    static{
        obstacleTiles = VirtualWorld.loadImages("obstacle", "O");
    }
    public Obstacle(Point position, List<Tile> tiles)
    {
        super(position, tiles);
    }


}
