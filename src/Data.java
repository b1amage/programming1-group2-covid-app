/*
  Class: Data
  Purpose: Use to store data of the processed csv file
  Contributors: Quoc Bao, Kha Tuan, Minh Long
  Created date: 25/7/2021
  Last modified: 29/8/2021
  Version 1.0
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Data {
    // Fields
    private static ArrayList<Row> rows; // Whole data, use static for avoid re-create everytime create object

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

    // Error field for user interface
    private boolean hasTimeRangeError = false;

    // Empty constructor
    public Data() {}

    // Constructors
    public Data(String location, TimeRange timeRange) {
        this.location = location;
        this.timeRange = timeRange;
    }

    /**
     * This method create a Data object base on location and object TimeRange
     * @param location location of the country
     * @param timeRange TimeRange object
     * @return new Data object
     */
    public static Data createData(String location, TimeRange timeRange) {
        // Create new data object to process
        return new Data(location, timeRange);
    }

    /**
     * This method create data from a timeRange based on user input
     */
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

            // If location not exist, throw a message and return
            if (isLocationNotExist()) {
                System.out.println("=========");
                System.out.println("Location not found");
                rowsFromStartDate = null;
                return;
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
                System.out.println("=========");
                System.out.println("Error in date, please try again");
                hasTimeRangeError = true;
                rowsFromStartDate = null;
            }

        } else { // User choose option (2) or (3)
            // Check if date exist
            int start = -1;
            for (Row row : rows) {
                // If start date and location match, assign the index to the start
                if (row.getDate().equals(timeRange.getStartDate()) && row.getLocation().equals(location)) {
                    start = rows.indexOf(row);
                }
            }

            // If location not exist, throw a message and return
            if (isLocationNotExist()) {
                System.out.println("=========");
                System.out.println("Location not found");
                rowsFromStartDate = null;
                return;
            }

            // No date found
            if (start == -1) {
                System.out.println("Error in date, please try again");
                rowsFromStartDate = null;
                hasTimeRangeError = true;
                return;
            }

            // If location exist
            for (Row row : rows) { // Loop through rows

                // If start date and location match, start processing array list
                if (row.getDate().equals(timeRange.getStartDate()) && (row.getLocation().equals(location))) {

                    // Get min value between rows.indexOf(row) and rows.indexOf(row) + nextDayCount (case negative nextDayCount)
                    for (int i = Math.min(rows.indexOf(row),rows.indexOf(row) + timeRange.getNextDayCount()); i <= Math.max(rows.indexOf(row),rows.indexOf(row) + timeRange.getNextDayCount()); i++) {

                        // Check if i is out of range
                        if (i >= rows.size() || i < 0) {
                            rowsFromStartDate = null;
                            hasTimeRangeError = true;
                            System.out.println("Date out of range, please try again");
                            break;

                        } else {
                            if (rows.get(i) != null) { // Add if the row is not null
                                // Check if the data of location is end
                                if (rows.get(i).getLocation().equals(location)) {
                                    rowsFromStartDate.add(rows.get(i));

                                } else if (!rows.get(i).getLocation().equals(location)){ // Difference in location
                                    rowsFromStartDate = null;
                                    System.out.println("Date out of range, please try again");
                                    hasTimeRangeError = true;
                                    break;
                                }
                            }
                        }
                    }
                    // Break after loop to save time
                    break;
                }
            }
        }
    }

    /**
     * This method is use to check if the location is not exist
     * @return true or false
     */
    private boolean isLocationNotExist() {
        for (Row row : rows) {
            if (row.getLocation().equals(location)) {
                    return false;
            }
        }
        return true;
    }

    public boolean isLocationNotExist(String location) {
        for (Row row : rows) {
            if (row.getLocation().equals(location)) {
                return false;
            }
        }
        return true;
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

    public boolean isHasTimeRangeError() {
        return hasTimeRangeError;
    }

}

/*
  Class: DataProcess
  Purpose: Use to process data from csv file and for Data class usage
  Contributors: Quoc Bao
  Created date: 23/7/2021
  Last modified: 26/8/2021
  Version 1.0
 */

class DataProcess {

    /**
     * This function create the row list from csv file
     * @return ArrayList rows
     * @throws FileNotFoundException the file is not exist
     */
    public static ArrayList<Row> createRowList() throws FileNotFoundException {
        // Read CSV file to an array list
        ArrayList<String> dataByRow = readCsvFile("src/data.csv");

        // Get title
        String[] title = getTitle(dataByRow);

        // Array list to add Row object
        ArrayList<Row> rows = new ArrayList<>();

        // Temporary variable
        String[] tempData;
        HashMap<String, String> tempMap = new HashMap<>();

        // Loop through rows
        for (int i = 1; i < dataByRow.size(); i++) {
            // Split row by the commas
            tempData = dataByRow.get(i).split(",");

            // Check if there is 8 components (including empty value)
            if (tempData.length == title.length) {
                for (int j = 0; j < title.length; j++) {
                    // Get key and value to a temporary map
                    tempMap.put(title[j], tempData[j]);
                }
            }

            // Add row object to array list
            rows.add(createNewRowFromMap(tempMap));
        }

        // Process the vaccinated people
        processVaccinatedPeople(rows);

        return rows;
    }

    /**
     * This method process the vaccinated people to the new vaccinated people as in csv it is the accumulate type
     * @param rows: the ArrayList to process the vaccinated people
     */
    public static void processVaccinatedPeople(ArrayList<Row> rows) {
        ArrayList<Integer> processedVacinatedPeople = new ArrayList<>();

        // This loop add the new people vaccinated to an arrayList
        for (int i = 1; i < rows.size(); i++) {
            if (rows.get(i).getPeopleVaccinated() != 0 && rows.get(i).getLocation().equals(rows.get(i-1).getLocation())) {
                int indexNearestNonZero = getNearestCaseNotZeroIndex(rows, i);
                if (indexNearestNonZero != -1) {
                    processedVacinatedPeople.add(rows.get(i).getPeopleVaccinated() - rows.get(indexNearestNonZero).getPeopleVaccinated());
                }
            }
        }

        // Index of array list to avoid out of bound exception
        int idxProcess = 0;

        // This loop use to set each value in array list to the rows
        for (int i = 1; i < rows.size(); i++) {
            if (rows.get(i).getPeopleVaccinated() != 0 && rows.get(i).getLocation().equals(rows.get(i-1).getLocation())) {
                int indexNearestNonZero = getNearestCaseNotZeroIndex(rows, i);
                if (indexNearestNonZero != -1) {
                    rows.get(i).setPeopleVaccinated(processedVacinatedPeople.get(idxProcess));
                    idxProcess++;
                }
            }
        }
    }

    private static int getNearestCaseNotZeroIndex(ArrayList<Row> rows, int currentIndex) {
        for (int i = currentIndex - 1; i > -1; i--) {
            if (!rows.get(i).getLocation().equals(rows.get(currentIndex).getLocation())) break;
            if (rows.get(i).getPeopleVaccinated() != 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * This method is to create a Row object from a HashMap
     * @param map: a hash map to store the key and value of data processed below
     * @return a Row object
     */
    public static Row createNewRowFromMap(HashMap<String, String> map) {
        return new Row(map.get("continent"), map.get("date"), map.get("people_vaccinated"), map.get("new_cases"),
                map.get("new_deaths"), map.get("location"), map.get("iso_code"), map.get("population"));
    }

    public static String[] getTitle(ArrayList<String> dataByRow) {
        return dataByRow.get(0).split(",");
    }

    /**
     * This method is used to read the csv file
     * @param pathName: the absolute path of the file
     * @return ArrayList of each row in the csv file
     * @throws FileNotFoundException the file is not exist
     */
    public static ArrayList<String> readCsvFile(String pathName) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(pathName));
        ArrayList<String> dataByRow = new ArrayList<>();

        while (sc.hasNextLine()) {
            dataByRow.add(sc.nextLine());
        }

        return dataByRow;
    }

    public static void displayRows(ArrayList<Row> rowArrayList) {
        for (Row row : rowArrayList) {
            System.out.println(row);
        }
    }
}

/*
  Class: TimeRange
  Purpose: Use to provide kind of TimeRange based on user's choice
  Contributors: Minh Long, Quoc Bao, Kha Tuan, Anh Duy
  Created date: 12/8/2021
  Last modified: 29/8/2021
  Version 1.0
 */
class TimeRange {

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
            case 1: // Use start and end date, so nextDayCount is 0
                System.out.println("Enter start date");
                String startDate = sc.nextLine();

                System.out.println("Enter end date");
                String endDate = sc.nextLine();
                return new TimeRange(startDate, endDate, 0);

            case 2: //User enter the date days and choose how many days or week from the start
            case 3: //User enter the date days and choose how many days or week from the end

                // Print suitable message
                if (timeRangeChoice == 2) {
                    System.out.println("Enter your start date: ");
                } else {
                    System.out.println("Enter your end date: ");
                }

                startDate = sc.nextLine();

                System.out.println("Use (1) weeks or (2) days");

                // We use string to avoid bug if user enter a string, we will parse the value later
                String daysOrWeeksChar = sc.nextLine().trim();

                while (!daysOrWeeksChar.equals("1") && !daysOrWeeksChar.equals("2")) {
                    System.out.println("Wrong option, please insert again: ");
                    daysOrWeeksChar = sc.nextLine().trim();
                }

                int daysOrWeeks = Integer.parseInt(daysOrWeeksChar);

                if (daysOrWeeks == 1) {
                    System.out.println("Enter your weeks");
                    String nextDayCountString = sc.nextLine().trim();

                    while (!nextDayCountString.matches("[0-9]+") || nextDayCountString.equals("0")) {
                        System.out.println("This is not a valid number, please insert again: ");
                        nextDayCountString = sc.nextLine().trim();
                    }

                    nextDayCount = Integer.parseInt(nextDayCountString) * 7;

                } else {
                    System.out.println("Enter your days");
                    String nextDayCountString = sc.nextLine().trim();

                    while (!nextDayCountString.matches("[0-9]+") || nextDayCountString.equals("0")) {
                        System.out.println("This is not a valid number, please insert again: ");
                        nextDayCountString = sc.nextLine().trim();
                    }

                    nextDayCount = Integer.parseInt(nextDayCountString);
                }
                // Use ternary operator to decide which object returned
                return timeRangeChoice == 2 ? new TimeRange(startDate, null, nextDayCount) : new TimeRange(startDate, null, -nextDayCount);
        }
        // No case found, we don't use default for easier upgrading
        return null;
    }
}



