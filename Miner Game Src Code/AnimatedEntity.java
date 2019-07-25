import java.util.List;
import edu.calpoly.spritely.Tile;
public abstract class AnimatedEntity extends ActiveEntity implements Animatable
{


    protected final int animationPeriod;

    public AnimatedEntity(Point position, List<Tile> tiles, int actionPeriod, int animationPeriod)
    {
        super(position, tiles, actionPeriod);
        this.animationPeriod = animationPeriod;
    }

    @Override
    public void nextFrame()
    {
        this.tileIndex = (this.tileIndex + 1) % this.tiles.size();
    }

    @Override
    public Action createAnimationAction(int repeatCount)
    {
        return new Animation(this, repeatCount);
    }


    @Override
    public int getAnimationPeriod()
    {
        return this.animationPeriod;
    }

}
