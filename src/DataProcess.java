import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class DataProcess {

    public static void main(String[] args) throws IOException {

        // Read CSV file to an array list
        ArrayList<String> dataByRow = readCsvFile("src/data.csv");

        // Get title
        String[] title = dataByRow.get(0).split(",");

        // Array list to add Row object
        ArrayList<Row> rows = new ArrayList<Row>();

        // Hash map to temporary row
        HashMap<String, String> tempMap = new HashMap<String, String>();

        // Temporary variable
        String[] tempData;
        Row tempRow = new Row();

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

            tempRow = createNewRowFromMap(tempMap);

            // Add row object to array list
            rows.add(tempRow);
        }

        // Print processed data
        displayRows(rows);
    }

    public static Row createNewRowFromMap(HashMap<String, String> map) {
        return new Row(map.get("continent"), map.get("date"), map.get("people_vaccinated"), map.get("new_cases"),
                map.get("new_deaths"), map.get("location"), map.get("iso_code"), map.get("population"));
    }

    public static ArrayList<String> readCsvFile(String pathName) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(pathName));
        ArrayList<String> dataByRow = new ArrayList<String>();

        while (sc.hasNextLine()) {
            dataByRow.add(sc.nextLine());
        }

        return dataByRow;
    }

    public static void displayRows(ArrayList<Row> rowArrayList) {
        for (Row row : rowArrayList) {
            row.display();
        }
    }

}
