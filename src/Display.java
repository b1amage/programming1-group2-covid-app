import java.util.*;

public class Display {
    private Summary summary;
    private String display;

    public Display(Summary summary, String display) {
        setSummary(summary);
        setDisplay(display);
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public static Display createDisplay(Summary summary, String display) {
        return new Display(summary, display);
    }

    /*
     This method display the data based on user's choice
     */
    public void createDisplay() {
        switch (display) {
            case "tabular":
                tabularDisplay(summary);
                break;
            case "chart":
                chartDisplay(summary);
                break;
            default:
                System.out.println("Cannot display this chart");
                break;
        }
    }

    /**
     * This method displays the data by using table
     * @param summary : the Summary object used to display
     */
    public void tabularDisplay(Summary summary) {
        System.out.printf("\n%s\n", "TABULAR DISPLAY");

        // Get the longest length of group name
        int maxLengthOfGroupName = 0;
        for (String groupName : summary.getGroupings().keySet()) {
            if (groupName.length() > maxLengthOfGroupName) {
                maxLengthOfGroupName = groupName.length();
            }
        }

        // Print the titles of the table
        System.out.println("Range" + " ".repeat(maxLengthOfGroupName) + "Value");

        // Print the group name and the value of each group with appropriate space between them
        for (String groupName : summary.getGroupings().keySet()) {
            System.out.println(groupName + " ".repeat(5 + (maxLengthOfGroupName - groupName.length())) + summary.getGroupings().get(groupName));
        }
    }

    /**
     * This method displays the data by using chart
     * @param summary: the Summary object used to display
     */
    public void chartDisplay(Summary summary) {
        int numOfRows = 24;
        int numOfCols = 80;

        // LinkedHashMap contains the keys are the group names and the values are the results of each group
        LinkedHashMap<String, Integer> summaryData = summary.getGroupings();
        int numOfGroups = summaryData.size();

        // Get the name of value displayed in the chart
        String valueName;
        switch (summary.getMetricType()) {
            case "positive cases":
                valueName = "Positive Cases";
                break;
            case "new deaths":
                valueName = "Deaths";
                break;
            case "people vaccinated":
                valueName = "People Vaccinated";
                break;
            default:
                valueName = "Metric Type";
                break;
        }

        // Use SortedSet to sort the result of each group and prevent duplicate results.
        SortedSet<Integer> summaryResults = new TreeSet<>();
        for (String groupName : summaryData.keySet()) {
            summaryResults.add(summaryData.get(groupName));
        }

        int smallestResult = summaryResults.first();
        int largestResult = summaryResults.last();

        // Check if there is no data or the data is too big to use chart display
        if (numOfGroups == 0 || numOfGroups > 26) {
            System.out.println("=========");
            System.out.println("There is no data or the data is to large to display by chart");
            display = null;
            return;
        }

        // Store all the group names of the data
        ArrayList<String> groups = new ArrayList<>(summaryData.keySet());

        // Get the longest length of the result
        int maxLengthOfResult = Integer.toString(summaryResults.last()).length();

        // This variable stores the number of spaces between 2 positions of group on X axis
        int spaceBetweenLabelOnX_axis;
        if (numOfGroups == 1) {
            spaceBetweenLabelOnX_axis = (numOfCols - 2) / 2;
        } else {
            spaceBetweenLabelOnX_axis = (numOfCols - numOfGroups - 1) / (numOfGroups - 1);
        }

        // This LinkedHashMap stores the positions of groups on X axis and the result of each group
        LinkedHashMap<Integer, Integer> mapPositionOnXtoGroup = new LinkedHashMap<>();
        int index = 0;
        for (String groupName : groups) {
            if (numOfGroups == 1) {
                mapPositionOnXtoGroup.put(spaceBetweenLabelOnX_axis, summaryData.get(groupName));
                continue;
            }

            // Map the position of group on X axis to its result
            mapPositionOnXtoGroup.put((spaceBetweenLabelOnX_axis + 1) * index, summaryData.get(groupName));
            index++;
        }

        System.out.printf("\n%s\n", "CHART DISPLAY");
        System.out.println(" ".repeat(maxLengthOfResult) + "\t" + valueName);

        for (int rowIndex = numOfRows - 1; rowIndex >= 0; rowIndex--) {
            // Variable to store the result which is the label at the current row
            int resultOnThisRow = -1;

            // Variable to store the position of a result on Y axis
            int positionOfLabelOnY_axis = -1;

            // If there is only one result, place it at the lowest position in the chart
            if (summaryResults.size() == 1) {
                positionOfLabelOnY_axis = 1;
            } else if (summaryResults.size() > 1 && summaryResults.first().equals(summaryResults.last())) {
                // If all of the results are the same, place them at the lowest position in the chart
                positionOfLabelOnY_axis = 1;
            } else if (summaryResults.size() > 1 && !summaryResults.first().equals(summaryResults.last())){
                // if the results are different, calculate the position of each result on Y axis
                positionOfLabelOnY_axis = ((summaryResults.last() - smallestResult) * (numOfRows - 2)) / (largestResult - smallestResult) + 1;
            }

            // If the position of a result on Y axis equals to the current row index
            if (rowIndex == positionOfLabelOnY_axis) {
                // Then the label of the current row will be that value
                resultOnThisRow = summaryResults.last();

                // Print out the label for the current row
                System.out.print(resultOnThisRow + " ".repeat(maxLengthOfResult - Integer.toString(resultOnThisRow).length()) + "\t|");

                // Remove the last result to iterate through all of the smaller results
                summaryResults.remove(summaryResults.last());
            } else if (rowIndex + 1 == positionOfLabelOnY_axis) {
                // If the position of a result on Y axis equals to the row index above, remove this result to iterate to the smaller results
                // This is because this result has the same position on Y axis as the larger result
                summaryResults.remove(summaryResults.last());
            }

            for (int columnIndex = 0; columnIndex < numOfCols; columnIndex++) {

                // If the current column index is 0 and there is no label here, print out the appropriate whitespace for indentation
                if (columnIndex == 0 && rowIndex != positionOfLabelOnY_axis) {
                    System.out.print(" ".repeat(maxLengthOfResult) + "\t|");
                    continue;
                }

                // If the current row index is at the end of the chart, print out '_' as a line for X axis
                if (rowIndex == 0) {
                    System.out.print('_');
                    continue;
                }

                // If the position of a result on Y axis equals to the current row index
                if (rowIndex == positionOfLabelOnY_axis) {
                    // Check if the current column index is the position of a group on X axis
                    if (mapPositionOnXtoGroup.containsKey(columnIndex)) {
                        // If the result of this group equals to the label at the current row, print out "*" to mark this position
                        int resultOfThisGroup = mapPositionOnXtoGroup.get(columnIndex);
                        if (resultOfThisGroup == resultOnThisRow) {
                            System.out.print('*');
                            continue;
                        } else {
                            // If the position of this group on Y axis equals to the current row index, print out "*" to mark this position
                            int positionOfThisGroup = ((resultOfThisGroup - smallestResult) * (numOfRows - 2)) / (largestResult - smallestResult) + 1;
                            if (positionOfThisGroup == rowIndex) {
                                System.out.print('*');
                                continue;
                            }
                        }
                    }
                }

                // If none of the above conditions are true, print out space
                System.out.print(' ');
            }

            // Move to the next line after iterating through all of the columns in a row
            System.out.println();
        }

        // Print out all of the group numbers and place them at the appropriate positions
        for (int i = 0; i < numOfGroups; i++) {
            // If this is the first group
            if (i == 0) {
                // If there is only one group, place it at the center
                if (numOfGroups == 1) {
                    System.out.print(" ".repeat(maxLengthOfResult) + "\t " + " ".repeat(spaceBetweenLabelOnX_axis) + (i + 1));
                } else {
                    // If not, place it at the first column in the chart
                    System.out.print(" ".repeat(maxLengthOfResult) + "\t " + (i + 1));
                }
                continue;
            }

            // If the group number has more than 2 digits, place them appropriately
            if (Integer.toString(i).length() > 1) {
                System.out.print(" ".repeat(spaceBetweenLabelOnX_axis - Integer.toString(i).length() + 1) + (i + 1));
                continue;
            }

            // Place all of the group numbers with equal spaces between them
            System.out.print(" ".repeat(spaceBetweenLabelOnX_axis) + (i + 1));
        }

        System.out.println();
        System.out.println(" ".repeat(maxLengthOfResult + 1) + "\t" + " ".repeat(numOfCols / 2 - 5) + "Group");

        // Print out the group number and the group name that it represents
        for (int i = 0; i < numOfGroups; i++) {
            System.out.printf("Group %d: %s\n", i + 1, groups.get(i));
        }
    }
}
