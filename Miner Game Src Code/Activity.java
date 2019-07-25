
final class Activity implements Action
{
    private final ActiveEntity activeEntity;
    private final WorldModel world;
    private final int repeatCount;
     // A repeat count of 0 means to repeat forever

    public Activity(ActiveEntity activeEntity, WorldModel world,
                  int repeatCount)
    {
        this.activeEntity = activeEntity;
        this.world = world;
        this.repeatCount = repeatCount;
    }
    public void executeAction(EventSchedule eventSchedule)
    {
        this.activeEntity.executeActivity(world, eventSchedule);
    }

}
