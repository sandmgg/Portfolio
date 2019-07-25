
final class DummyAnimation implements Animatable
{
    public int count;

    public DummyAnimation()
    {
        this.count = 0;
    }
    public void nextFrame()
    {
        this.count+=1;
    }
    public Action createAnimationAction(int repeatCount)
    {
        return new Animation(this, repeatCount);
    }
    public int getAnimationPeriod()
    {
        return 1;
    }

}
