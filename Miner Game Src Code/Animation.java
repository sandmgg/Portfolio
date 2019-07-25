import edu.calpoly.spritely.SolidColorTile;
import java.awt.Color;
import edu.calpoly.spritely.AnimationFrame;

final class Animation implements Action
{
    private final Animatable animatable;
    private final int repeatCount;
    // A repeat count of 0 means to repeat forever

    public Animation(Animatable entity, int repeatCount)
    {
        this.animatable = entity;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventSchedule eventSchedule)
    {
        this.animatable.nextFrame();

        if (this.repeatCount != 1)
        {
            eventSchedule.scheduleEvent(this.animatable,
                this.animatable.createAnimationAction(
                    Math.max(this.repeatCount - 1, 0)),
                this.animatable.getAnimationPeriod());
        }
    }


    /*public void changeColor(EventSchedule eventSchedule)
    {
        //Tile yellow = new SolidColorTile(new Color(255,255,0,0.5* (1.0 -cos(theta))), 'S');
        //Tile grass = getImageTile("grass.png", '.');
        Alpha alpha = new Alpha();

        //Tile[][] grasses = new Tile[VirtualWorld.WORLD_SIZE.height][VirtualWorld.WORLD_SIZE.width];
        for (int y = 0; y < VirtualWorld.WORLD_SIZE.height; y++) {
            for (int x = 0; x < VirtualWorld.WORLD_SIZE.width; x++) {
                Tile c = VirtualWorld.BACKGROUND[y][x];
                if (c == VirtualWorld.grass) {
                    frame.addTile(x,y, new SolidColorTile(new Color(255, 255, 0, alpha.returnAlpha()), 's'));
                    alpha.changeAlpha();
                    eventSchedule.scheduleEvent(null, this, .1);
                    //grasses[y][x] = grass;
                }

            }
        }

    }*/


}
