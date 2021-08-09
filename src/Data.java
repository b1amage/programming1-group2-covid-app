import java.io.FileNotFoundException;
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

    private ArrayList<Row> rowsFromStartDate = new ArrayList<Row>(); // Data of the area and time range
    private String continent;
    private String country;
    private String startDate;
    private String endDate;
    private int nextDayCount; // Use to store the next day (positive int) or previous day (negative int)

    // Empty constructor
    public Data() throws FileNotFoundException {}

    // Constructors
    public Data(String continent, String country, String startDate, String endDate) throws FileNotFoundException {
        this.continent = continent;
        this.country = country;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Data(String continent, String country, String startDate, int nextDayCount) throws FileNotFoundException {
        this.continent = continent;
        this.country = country;
        this.startDate = startDate;
        this.nextDayCount = nextDayCount;
    }

    public static Data createData() throws FileNotFoundException {
        // Create new data object to process
        Data data = new Data();

        // Ask for country or continent
        String[] countryAndContinent = areaInput();

        // Set values to the data object
        data.setCountry(countryAndContinent[0]);
        data.setContinent(countryAndContinent[1]);

        // Ask kind of date
        String[] dateInformation = dateOptionInput();

        // Use setters to set value to the data
        data.setStartDate(dateInformation[0]);
        data.setEndDate(dateInformation[1]);
        data.setNextDayCount(Integer.parseInt(dateInformation[2]));

        return data;
    }

    public void createRowData() {
        if (endDate != null) { // If user use option (1) start date and end date
            // Set initial to -1
            int startIndex = -1;
            int endIndex = -1;

            // Find start and end index
            for (Row row : rows) {
                // If start date and (continent or country) match, assign the index to the start
                if (row.getDate().equals(startDate) && (row.getContinent().equals(continent) || row.getLocation().equals(country))) {
                    startIndex = rows.indexOf(row);
                }
                // If end date and (continent or country) match, assign the index to the start
                if (row.getDate().equals(endDate) && (row.getContinent().equals(continent) || row.getLocation().equals(country))) {
                    endIndex = rows.indexOf(row);
                }
            }

            // If start and end index found
            if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
                // Add each row to the array list
                for (int i = startIndex; i <= endIndex; i++) {

                    // Check if the data of country or continent is end
                    if (rows.get(i).getContinent().equals(continent) || rows.get(i).getLocation().equals(country)) {
                        rowsFromStartDate.add(rows.get(i));
                    }
                }
            } else { // No start or end date found
                System.out.println("Error in date, country, or continent");
            }

        } else { // User choose option (2) or (3)
            for (Row row : rows) { // Loop through rows

                // If start date and (continent or country) match, start processing array list
                if (row.getDate().equals(startDate) && (row.getContinent().equals(continent) || row.getLocation().equals(country))) {

                    // Get min value between rows.indexOf(row) and rows.indexOf(row) + nextDayCount (case negative nextDayCount)
                    for (int i = Math.min(rows.indexOf(row),rows.indexOf(row) + nextDayCount); i <= Math.max(rows.indexOf(row),rows.indexOf(row) + nextDayCount) && i < rows.size() && i > -1; i++) {

                        if (rows.get(i) != null) { // Add if the row is not null
                            // Check if the data of country or continent is end
                            if (rows.get(i).getContinent().equals(continent) || rows.get(i).getLocation().equals(country)) {
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
     * @return String[] {country, continent}
     */
    private static String[] areaInput() {
        Scanner sc = new Scanner(System.in);
        String country;
        String continent;

        System.out.println("Use country (1) or continent (2)?");
        int areaChoice = Integer.parseInt(sc.nextLine());

        // If user use country, set continent to empty string, and vice versa
        if (areaChoice == 1) {
            System.out.println("Enter country: ");
            country = sc.nextLine();
            continent = "";
        } else {
            System.out.println("Enter continent: ");
            continent = sc.nextLine();
            country = "";
        }

        return new String[] {country, continent};
    }

    /**
     * This method is used to get user date option input
     * @return String[] {startDate, endDate, nextDayCount}
     */
    private static String[] dateOptionInput() {
        showDateChoiceMenu();
        // Get option
        Scanner sc = new Scanner(System.in);
        int dateChoice = Integer.parseInt(sc.nextLine());

        // Initialize necessary variable and set default values to them
        String startDate = null;
        String endDate = null;
        String day = "0"; // Set day to 0 String to avoid null pointer exception when user use option 1;
        int dayOrWeekChoice;

        switch (dateChoice) {
            case 1: // Use start date and end date
                System.out.println("Enter start date: ");
                startDate = sc.nextLine();
                System.out.println("Enter end date: ");
                endDate = sc.nextLine();
                break;

            case 2: // Use start date and next day
                System.out.println("Enter date: ");
                startDate = sc.nextLine();

                // Ask user to use week or day
                System.out.println("Use week (1) or day (2)?");
                dayOrWeekChoice = Integer.parseInt(sc.nextLine());

                // Convert week to day and assign to the variable day
                if (dayOrWeekChoice == 1) {
                    System.out.println("Enter weeks: ");
                    day = String.valueOf(Integer.parseInt(sc.nextLine()) * 7);
                } else {
                    System.out.println("Enter days: ");
                    day = String.valueOf(Integer.parseInt(sc.nextLine()));
                }
                break;

            case 3: // Use start date and previous day (set nextDayCount to negative value)
                System.out.println("Enter date: ");
                startDate = sc.nextLine();

                // Ask user to use week or day
                System.out.println("Use week (1) or day (2)?");
                dayOrWeekChoice = Integer.parseInt(sc.nextLine());

                // Convert week to day and assign to the variable day
                if (dayOrWeekChoice == 1) {
                    System.out.println("Enter weeks: ");
                    day = String.valueOf(-Integer.parseInt(sc.nextLine()) * 7);
                } else {
                    System.out.println("Enter days: ");
                    day = String.valueOf(-Integer.parseInt(sc.nextLine()));
                }
                break;
        }
        return new String[] {startDate, endDate, day};
    }

    /**
     * Display method to show all attributes in String
     */
    public void display() {
        System.out.println("Continent: " + continent);
        System.out.println("Country: " + country);
        System.out.println("Start date: " + startDate);
        System.out.println("End date: " + endDate);
        System.out.println("Next day count: " + nextDayCount);
    }

    // Getters and Setters
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

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getNextDayCount() {
        return nextDayCount;
    }

    public void setNextDayCount(int nextDayCount) {
        this.nextDayCount = nextDayCount;
    }

    public ArrayList<Row> getRows() {
        return rows;
    }

    public ArrayList<Row> getRowsFromStartDate() {
        return rowsFromStartDate;
    }
}
