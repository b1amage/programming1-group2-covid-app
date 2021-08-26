import java.util.Scanner;

public class TimeRange {
    protected String startDate;
    protected String endDate;
    protected int nextDayCount;

    public TimeRange(String startDate, String endDate, int nextDayCount) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.nextDayCount = nextDayCount;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getNextDayCount() {
        return nextDayCount;
    }

    public void setNextDayCount(int nextDayCount) {
        this.nextDayCount = nextDayCount;
    }

    public void display() {
        System.out.println("Start date: " + startDate);
    }

    public static TimeRange setTimeRangeFromChoice(int timeRangeChoice) {
        Scanner sc = new Scanner(System.in);
        int nextDayCount;
        TimeRange timeRange;
        switch (timeRangeChoice) {
            case 1:
                System.out.println("Enter start date");
                String startDate = sc.nextLine();

                System.out.println("Enter end date");
                String endDate = sc.nextLine();
                return new TimeRange(startDate, endDate, 0);

            case 2:
            case 3: //User enter the date days and choose how many days or week from the start
                System.out.println("Enter your start date");
                startDate = sc.nextLine();

                System.out.println("Use (1) weeks or (2) days");
                String daysOrWeeksChar = sc.nextLine().trim();
                while (!daysOrWeeksChar.equals("1") && !daysOrWeeksChar.equals("2")) {
                    System.out.println("Wrong option, please insert again: ");
                    daysOrWeeksChar = sc.nextLine().trim();
                }

                int daysOrWeeks = Integer.parseInt(daysOrWeeksChar);

                if (daysOrWeeks == 1) {
                    System.out.println("Enter your weeks");
                    nextDayCount = Integer.parseInt(sc.nextLine()) * 7;
                } else {
                    System.out.println("Enter your days");
                    nextDayCount = Integer.parseInt(sc.nextLine());
                }
                timeRange = new TimeRange(startDate, null, nextDayCount);
                return timeRangeChoice == 2 ? new TimeRange(startDate, null, nextDayCount) : new TimeRange(startDate, null, -nextDayCount);
        }
        return null;
    }
}


