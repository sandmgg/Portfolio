import java.util.List;
import edu.calpoly.spritely.Tile;
public class Blacksmith extends Entity
{
    public static final List<Tile> blacksmithTiles;
    static{
        blacksmithTiles = VirtualWorld.loadImages("blacksmith", "B");
    }
    public Blacksmith(Point position, List<Tile> tiles)
    {
        super(position, tiles);
    }

}
