
public interface Animatable
{
    public void nextFrame();
    public Action createAnimationAction(int repeatCount);
    public int getAnimationPeriod();
}
