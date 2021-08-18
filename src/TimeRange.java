public abstract class TimeRange {
    protected String startDate;

    public TimeRange(String startDate) {
        this.startDate = startDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void display() {
        System.out.println("Start date: " + startDate);
    }

    abstract public String getEndDate();

    abstract public int getNextDayCount();


}

class StartAndEndDate extends TimeRange{
    protected String endDate;

    public StartAndEndDate(String startDate, String endDate) {
        super(startDate);
        this.endDate = endDate;
    }

    @Override
    public String getEndDate() {
        return endDate;
    }

    @Override
    public int getNextDayCount() {
        return 0;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}

class PreviousDay extends TimeRange{
    protected int nextDayCount;
    public PreviousDay(String startDate, int nextDayCount) {
        super(startDate);
        this.nextDayCount = -nextDayCount;
    }

    @Override
    public String getEndDate() {
        return null;
    }

    @Override
    public int getNextDayCount() {
        return nextDayCount;
    }

    public void setNextDayCount(int nextDayCount) {
        this.nextDayCount = nextDayCount;
    }
}

class NextDay extends TimeRange{
    protected int nextDayCount;
    public NextDay(String startDate, int nextDayCount) {
        super(startDate);
        this.nextDayCount = nextDayCount;
    }

    @Override
    public String getEndDate() {
        return null;
    }

    public int getNextDayCount() {
        return nextDayCount;
    }

    public void setNextDayCount(int nextDayCount) {
        this.nextDayCount = nextDayCount;
    }
}

