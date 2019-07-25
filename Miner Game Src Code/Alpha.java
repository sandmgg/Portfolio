import java.lang.Math;

final class Alpha implements Animatable
{
    private double theta;
    public Alpha()
    {
        this.theta = 0;
    }
    public double returnAlpha()
    {
        return 0.5* (1.0 - Math.cos(this.theta));
    }
    public void nextFrame()
    {
        this.theta += 2* Math.PI/300;
    }
    public Action createAnimationAction(int repeatCount)
    {
        return new Animation(this, 0);
    }
    public int getAnimationPeriod()
    {
        return 100;
    }

}
