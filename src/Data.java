/*
  Class: Data
  Purpose: Use to store data of the processed csv file
  Contributors: Quoc Bao, Kha Tuan, Minh Long
  Created date: 25/7/2021
  Last modified: 26/8/2021
  Version 1.0
 */
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Data {
    // Fields
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

    /**
     * This method create a Data object base on location and object TimeRange
     * @param location location of the country
     * @param timeRange TimeRange object
     * @return new Data object
     * @throws IOException file not found
     */
    public static Data createData(String location, TimeRange timeRange) throws IOException {
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
                System.out.println("Error in date or location");
                rowsFromStartDate = null;
                return;
            }

        } else { // User choose option (2) or (3)
            // Check if location is exist
            boolean locationExist = false;

            for (Row row : rows) {
                if (row.getLocation().equals(location)) locationExist = true;
            }

            // If location not exist, throw a message and return
            if (!locationExist) {
                System.out.println("=========");
                System.out.println("Location not found");
                rowsFromStartDate = null;
                return;
            }

            // If location exist
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
