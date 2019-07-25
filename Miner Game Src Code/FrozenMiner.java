import java.util.List;
import edu.calpoly.spritely.Tile;
public class FrozenMiner extends Entity
{
    public static final List<Tile> frozenMinerTiles;
    static{
        frozenMinerTiles = VirtualWorld.loadImages("frozen_miner", "f");
    }
    public FrozenMiner(Point position, List<Tile> tiles)
    {
        super(position, tiles);
    }

}
