
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Predicate;

/**
 * This data stucture is used to hold the scheduled events that are queued
 * for later execution.
 * <p>
 * This event queue is used to drive all movement and animation within
 * the miner simulation.
 */
final class EventSchedule {

    /**
     * A list of all pending events.
     */
    private PriorityQueue<Event> pendingEvents;

    /**
     * The current time in ms, according to how far we've advanced.
     */
    private double currentTime;

    /**
     * Create a new EventSchedule.
     */
    public EventSchedule()
    {
        this.pendingEvents = new PriorityQueue<Event>((Event e1, Event e2) -> {
            if (e1.getTime() < e2.getTime())
                return -1;
            else if (e1.getTime() > e2.getTime())
                return 1;
            else
                return 0;});
        this.currentTime = 0.0;
    }

    /**
     * Get the time we've advanced to so far.  When an event's action is
     * being executed, this will be the time of that event.
     */
    public double getCurrentTime() {
        return currentTime;
    }

    /**
     * Schedule an event at or after the current time.
     *
     * @param target  The target of the event.  This can be used to
     *                unschedule all events for a given target.  It may
     *                be null.
     * @param action  The action to execute when the event is fired.
     * @param after   How far in the future, in ms.  Must be >= 0.
     *                The event will fire at getCurrentTime() + after.
     */
    public void scheduleEvent(Object target, Action action, double after) {
        assert after >= 0;
        //System.out.println("TODO:  Implement add event with " + action + " to "
        //                   + pendingEvents);
        Event newE = new Event(action, currentTime + after, target);

            pendingEvents.add(newE);
    }

    /**
     * Remove all of the events for the given target from the list of
     * pending events.  This should remove every event where
     * target.equals(event.getTarget()).  Only events with non-null
     * targets can be unscheduled with this method.
     */
    public void unscheduleAllEvents(Object target)
    {
        assert target != null;
        Predicate<Event> isUnschedule = (Event e) -> e.getTarget().equals(target);

        //System.out.println("TODO:  Implement remove all events for " + target);
                pendingEvents.removeIf(isUnschedule);
    }


    /**
     * Process all the pending events, in order, until we get to
     * the time advanceToTime (inclusive).  When this method completes,
     * getCurrentTime() will return advanceToTime.
     */
    public void processEvents(double advanceToTime)
    {
        assert advanceToTime >= currentTime;



        while (pendingEvents.size() > 0 && pendingEvents.peek().getTime() <= advanceToTime)
        {
                currentTime = pendingEvents.peek().getTime();
                pendingEvents.poll().execute(this);
                //pendingEvents.remove(i);
                //i--;


        }
        currentTime = advanceToTime;




        //System.out.println("TODO:  Implement advance to " + advanceToTime);
    }

    /**
     * Give the number of events currently scheduled.
     */
    public int size() {
        return pendingEvents.size();
    }

}
