import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Data {
    // Attributes
    private static ArrayList<Row> rows; // Whole data

    static {
        try {
            rows = DataProcess.createRowList();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Row> rowsFromStartDate = new ArrayList<>(); // Data of the area and time range
    private String location;
    private TimeRange timeRange;

    // Empty constructor
    public Data() {}

    // Constructors
    public Data(String location, TimeRange timeRange) {
        this.location = location;
        this.timeRange = timeRange;
    }

    public static Data createData() throws IOException {
        // Create new data object to process
        Data data = new Data();

        // Ask for location
        String location = areaInput();

        // Set location to the data object
        data.setLocation(location);

        // Ask kind of date
        TimeRange dateInformation = dateOptionInput();

        // Use setter to set value to the time range
        data.setTimeRange(dateInformation);

        return data;
    }

    public void createRowData() {
        if (timeRange.getEndDate() != null) { // If user use option (1) start date and end date
            // Set initial to -1
            int startIndex = -1;
            int endIndex = -1;

            // Find start and end index
            for (Row row : rows) {
                // If start date and location match, assign the index to the start
                if (row.getDate().equals(timeRange.getStartDate()) && row.getLocation().equals(location)) {
                    startIndex = rows.indexOf(row);
                }
                // If end date and location match, assign the index to the start
                if (row.getDate().equals(timeRange.getEndDate()) && row.getLocation().equals(location)) {
                    endIndex = rows.indexOf(row);
                }
            }

            // If start and end index found
            if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
                // Add each row to the array list
                for (int i = startIndex; i <= endIndex; i++) {

                    // Check if the data of location is end
                    if (rows.get(i).getLocation().equals(location)) {
                        rowsFromStartDate.add(rows.get(i));
                    }
                }
            } else { // No start or end date found
                System.out.println("Error in date or location");
            }

        } else { // User choose option (2) or (3)
            for (Row row : rows) { // Loop through rows

                // If start date and location match, start processing array list
                if (row.getDate().equals(timeRange.getStartDate()) && (row.getLocation().equals(location))) {

                    // Get min value between rows.indexOf(row) and rows.indexOf(row) + nextDayCount (case negative nextDayCount)
                    for (int i = Math.min(rows.indexOf(row),rows.indexOf(row) + timeRange.getNextDayCount()); i <= Math.max(rows.indexOf(row),rows.indexOf(row) + timeRange.getNextDayCount()) && i < rows.size() && i > -1; i++) {

                        if (rows.get(i) != null) { // Add if the row is not null
                            // Check if the data of location is end
                            if (rows.get(i).getLocation().equals(location)) {
                                rowsFromStartDate.add(rows.get(i));
                            }
                        }

                    }

                    // Break after loop to save time
                    break;
                }
            }
        }
    }

    public static void showDateChoiceMenu() {
        System.out.println("Enter your date choice: ");
        System.out.println("(1) A pair of start date and end date (inclusive) (e.g., 1/1/2021 and 8/1/2021)");
        System.out.println("(2) A number of days or weeks from a particular date (e.g., 2 days from 1/20/2021 " +
                "means there are 3 days 1/20/2021, 1/21/2021, and 1/22/2021)");
        System.out.println("(3) A number of days or weeks to a particular date " +
                "(e.g., 1 week to 1/8/2021 means there are 8 days from 1/1/2021 to 1/8/2021)");
    }

    /**
     * This method is used to get the area input from user
     * @return String location
     */
    private static String areaInput() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter location: ");

        return sc.nextLine().trim();
    }

    /**
     * This method is used to get user date option input
     * @return TimeRange
     */
    private static TimeRange dateOptionInput() {
        showDateChoiceMenu();

        // Get option
        Scanner sc = new Scanner(System.in);
        int dateChoice = Integer.parseInt(sc.nextLine());

        // Initialize necessary variable and set default values to them
        String startDate = null;
        String endDate;
        int day = 0; // Set day to 0 to avoid null pointer exception when user use option 1;
        TimeRange timeRange = null;
        int dayOrWeekChoice;

        switch (dateChoice) {
            case 1: // Use start date and end date
                System.out.println("Enter start date: ");
                startDate = sc.nextLine();
                System.out.println("Enter end date: ");
                endDate = sc.nextLine();

                timeRange = new StartAndEndDate(startDate, endDate);
                break;

            case 2:
            case 3:
                // Use start date and next day
                System.out.println("Enter date: ");
                startDate = sc.nextLine();

                // Ask user to use week or day
                System.out.println("Use week (1) or day (2)?");
                dayOrWeekChoice = Integer.parseInt(sc.nextLine());

                // Convert week to day and assign to the variable day
                if (dayOrWeekChoice == 1) {
                    System.out.println("Enter weeks: ");
                    day = Integer.parseInt(sc.nextLine()) * 7;
                } else {
                    System.out.println("Enter days: ");
                    day = Integer.parseInt(sc.nextLine());
                }
                // Use tertiary to decide if it is a NextDay (2) or PreviousDay (3)
                timeRange = dateChoice == 2 ? new NextDay(startDate, day) : new PreviousDay(startDate, day);
                break;
        }

        return timeRange;
    }

    /**
     * Display method to show all attributes in String
     */
    public void display() {
        System.out.println("Location: " + location);
        timeRange.display();
    }

    // Getters and Setters

    public ArrayList<Row> getRows() {
        return rows;
    }

    public ArrayList<Row> getRowsFromStartDate() {
        return rowsFromStartDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public TimeRange getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(TimeRange timeRange) {
        this.timeRange = timeRange;
    }
}
