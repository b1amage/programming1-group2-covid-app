/*
  Class: DataProcess
  Purpose: Use to process data from csv file and for Data class usage
  Contributors: Quoc Bao
  Created date: 23/7/2021
  Last modified: 26/8/2021
  Version 1.0
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class DataProcess {

    public static void main(String[] args) throws IOException {
        // Call createRowList method to create row
        ArrayList<Row> rows = createRowList();

        // Print processed data
        displayRows(rows);
    }

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

        // This loop use to fill those empty to 0
//        for (int i = 0; i < rows.size(); i++) {
//            if (rows.get(i).getPeopleVaccinated() == -1) {
//                rows.get(i).setPeopleVaccinated(0);
//            }
//        }

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
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i).getPeopleVaccinated() != 0 && i > 0 && rows.get(i).getLocation().equals(rows.get(i-1).getLocation())) {
                int indexNearestNonZero = getNearestCaseNotZeroIndex(rows, i);
                if (indexNearestNonZero != -1) {
                    rows.get(i).setPeopleVaccinated(processedVacinatedPeople.get(idxProcess));
                    idxProcess++;
                }
            }
        }
    }

    private static int getNearestCaseNotZeroIndex(ArrayList<Row> rows, int currentIndex) {
        for (int i = currentIndex; i > -1; i--) {
            if (rows.get(i).getPeopleVaccinated() != 0 && rows.get(i).getLocation().equals(rows.get(currentIndex).getLocation())) {
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
