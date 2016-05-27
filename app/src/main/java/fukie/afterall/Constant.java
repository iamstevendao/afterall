package fukie.afterall;

/**
 * Created by Fukie on 27/05/2016.
 */
public class Constant {
    public static final int EVENT_ANNIVERSARY = 1;
    public static final int EVENT_JOB = 2;
    public static final int EVENT_LIFE = 3;
    public static final int EVENT_TRIP = 4;
    public static final int EVENT_EDUCATION = 5;
    public static final int EVENT_OTHER = 6;

    public enum EventType {
        ANNIVERSARY, JOB, LIFE, TRIP, EDUCATION, OTHER
    }

    public static int getEventTypeId(String eventType) {
        return EventType.valueOf(eventType).ordinal() + 1;
    }
}
