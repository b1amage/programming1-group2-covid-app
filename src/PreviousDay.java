public class PreviousDay extends TimeRange{
    public PreviousDay(String startDate, int nextDayCount) {
        super(startDate, null, -nextDayCount);
    }

}
