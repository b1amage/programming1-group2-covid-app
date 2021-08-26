/*
  Class: TimeRange
  Purpose: Use to provide kind of TimeRange based on user's choice
  Contributors: Minh Long, Quoc Bao, Kha Tuan, Anh Duy
  Created date: 12/8/2021
  Last modified: 26/8/2021
  Version 1.0
 */

import java.util.Scanner;

public class TimeRange {

    // Fields
    private String startDate;
    private String endDate;
    private int nextDayCount;

    // Constructors
    public TimeRange(String startDate, String endDate, int nextDayCount) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.nextDayCount = nextDayCount;
    }

    // Getters and setters
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

    /**
     * This method help in printing semantic information of object
     * @return String description of object
     */
    public String toString() {
        return "Start date: " + startDate + ".\nEnd date: " + endDate + ".\nNext day count: " + nextDayCount + ".";
    }

    /**
     * This function create a TimeRange object for the UserInterface to process
     * @param timeRangeChoice: a number to display the time range use
     * @return TimeRange timeRange
     */
    public static TimeRange setTimeRangeFromChoice(int timeRangeChoice) {
        Scanner sc = new Scanner(System.in);
        int nextDayCount;
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

                return timeRangeChoice == 2 ? new TimeRange(startDate, null, nextDayCount) : new TimeRange(startDate, null, -nextDayCount);
        }
        return null;
    }
}


