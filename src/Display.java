/*
  Class: Display
  Purpose: Used to display the data after being processed
  Contributors: Kha Tuan
  Created date: 25/7/2021
  Last modified: 26/8/2021
  Version 1.0
 */
import java.util.*;

public class Display {
    private LinkedHashMap<String, Integer> groupings;
    private String metricType;
    private String displayMethod;
    private boolean validDisplay;

    public Display(LinkedHashMap<String, Integer> groupings, String metricType, String displayMethod) {
        setGroupings(groupings);
        setMetricType(metricType);
        setDisplay(displayMethod);
    }

    public void setGroupings(LinkedHashMap<String, Integer> groupings) {
        this.groupings = groupings;
    }

    public void setMetricType(String metricType) {
        this.metricType = metricType;
    }

    public void setDisplay(String display) {
        this.displayMethod = display;
    }

    public boolean getValidDisplay() {
        return validDisplay;
    }

    public static Display createDisplay(LinkedHashMap<String, Integer> groupings, String metricType, String displayMethod) {
        return new Display(groupings, metricType, displayMethod);
    }

    /*
     This method display the data based on user's choice
     */
    public void displayData() {
        switch (displayMethod) {
            case "tabular":
                DataDisplay tabularDisplay = new TabularDisplay(groupings);
                tabularDisplay.display();
                validDisplay = tabularDisplay.getValidDisplay();
                break;
            case "chart":
                DataDisplay chartDisplay = new ChartDisplay(groupings, metricType);
                chartDisplay.display();
                validDisplay = chartDisplay.getValidDisplay();
                break;
            default:
                System.out.println("Invalid type of display");
                break;
        }
    }
}

abstract class DataDisplay {
    protected LinkedHashMap<String, Integer> groupings;
    private boolean validDisplay = true;
    private String displayName;

    public DataDisplay() {
        groupings = new LinkedHashMap<>();
    }

    public void addDataPoint(String groupLabel, int result) {
        groupings.put(groupLabel, result);
    }

    public void setValidDisplay(boolean validDisplay) {
        this.validDisplay = validDisplay;
    }

    public boolean getValidDisplay(){
        return validDisplay;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setGroupings(LinkedHashMap<String, Integer> groupings) {
        for (String groupName : groupings.keySet()) {
            addDataPoint(groupName, groupings.get(groupName));
        }
    }

    public abstract void display();
}

class TabularDisplay extends DataDisplay {
    public TabularDisplay(LinkedHashMap<String, Integer> groupings) {
        super();
        setGroupings(groupings);
        setDisplayName("TABULAR DISPLAY");
    }

    @Override
    public void display() {
        System.out.printf("\n%s\n", getDisplayName());

        // Get the longest length of group name
        int maxLengthOfGroupName = 0;
        for (String groupName : groupings.keySet()) {
            if (groupName.length() > maxLengthOfGroupName) {
                maxLengthOfGroupName = groupName.length();
            }
        }

        // Print the titles of the table
        System.out.println("Range" + " ".repeat(maxLengthOfGroupName) + "Value");

        // Print the group name and the value of each group with appropriate space between them
        for (String groupName : groupings.keySet()) {
            System.out.println(groupName + " ".repeat(5 + (maxLengthOfGroupName - groupName.length())) + groupings.get(groupName));
        }
    }
}

class ChartDisplay extends DataDisplay {
    private final static int NUM_OF_ROWS = 24;
    private final static int NUM_OF_COLS = 80;
    private String metricType;

    public ChartDisplay(LinkedHashMap<String, Integer> groupings, String metricType) {
        super();
        setMetricType(metricType);
        setGroupings(groupings);
        setDisplayName("CHART DISPLAY");
    }

    public void setMetricType(String metricType) {
        this.metricType = metricType;
    }

    @Override
    public void display() {
        int numOfGroups = groupings.size();

        // Get the name of value displayed in the chart
        String valueName;
        switch (metricType) {
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
        SortedSet<Long> summaryResults = new TreeSet<>();
        for (String groupName : groupings.keySet()) {
            summaryResults.add((long)groupings.get(groupName));
        }

        // Check if there is no data or the data is too big to use chart display
        if (numOfGroups == 0 || numOfGroups > 26 || summaryResults.size() > NUM_OF_ROWS - 1) {
            System.out.println("=========");
            System.out.println("There is no data or the data is to large to display by chart");
            setValidDisplay(false);
            return;
        }

        // Store all the group names of the data
        ArrayList<String> groups = new ArrayList<>(groupings.keySet());

        // Get the longest length of the result
        int maxLengthOfResult = Long.toString(summaryResults.last()).length();

        // This variable stores the number of spaces between 2 positions of group on X axis
        int spaceBetweenLabelOnXaxis;
        if (numOfGroups == 1) {
            spaceBetweenLabelOnXaxis = (NUM_OF_COLS - 2) / 2;
        } else {
            spaceBetweenLabelOnXaxis = (NUM_OF_COLS - numOfGroups - 1) / (numOfGroups - 1);
        }

        // This LinkedHashMap stores the positions of groups on X axis and the result of each group
        LinkedHashMap<Integer, Integer> mapPositionOnXtoGroup = new LinkedHashMap<>();
        int index = 0;
        for (String groupName : groups) {
            if (numOfGroups == 1) {
                mapPositionOnXtoGroup.put(spaceBetweenLabelOnXaxis, groupings.get(groupName));
                continue;
            }

            // Map the position of group on X axis to its result
            mapPositionOnXtoGroup.put((spaceBetweenLabelOnXaxis + 1) * index, groupings.get(groupName));
            index++;
        }

        System.out.printf("\n%s\n", getDisplayName());
        System.out.println(" ".repeat(maxLengthOfResult) + "\t" + valueName);

        // Store summary results in LinkedList to iterate backward.
        LinkedList<Long> results = new LinkedList<>(summaryResults);

        for (int rowIndex = NUM_OF_ROWS - 1; rowIndex >= 0; rowIndex--) {
            long resultOnThisRow = 0;
            long positionOfLabelOnYaxis = 0;

            Iterator<Long> iterator = results.descendingIterator();
            while (iterator.hasNext()) {
                long result = iterator.next();

                // Calculate the position of a result on Y axis
                if (result == summaryResults.first()) {
                    positionOfLabelOnYaxis = 1;
                } else {
                    positionOfLabelOnYaxis = ((result - summaryResults.first()) * (NUM_OF_ROWS - 2)) / (summaryResults.last() - summaryResults.first()) + 1;
                }

                // If the current row index matches with the position of a result on Y axis, then print out that result as the label of the current row
                if (rowIndex == positionOfLabelOnYaxis) {
                    resultOnThisRow = result;
                    System.out.print(result + " ".repeat(maxLengthOfResult - Long.toString(result).length()) + "\t|");
                    break;
                }
            }

            for (int columnIndex = 0; columnIndex < NUM_OF_COLS; columnIndex++) {

                // If the current column index is 0 and there is no label here, print out the appropriate whitespace for indentation
                if (columnIndex == 0 && rowIndex != positionOfLabelOnYaxis) {
                    System.out.print(" ".repeat(maxLengthOfResult) + "\t|");
                    continue;
                }

                // If the current row index is at the end of the chart, print out '_' as a line for X axis
                if (rowIndex == 0) {
                    System.out.print('_');
                    continue;
                }

                // If the position of a result on Y axis equals to the current row index
                if (rowIndex == positionOfLabelOnYaxis) {
                    // Check if the current column index is the position of a group on X axis
                    if (mapPositionOnXtoGroup.containsKey(columnIndex)) {
                        // If the result of this group equals to the label at the current row, print out "*" to mark this position
                        int resultOfThisGroup = mapPositionOnXtoGroup.get(columnIndex);
                        if (resultOfThisGroup == resultOnThisRow) {
                            System.out.print('*');
                            continue;
                        } else {
                            // If the position of this group on Y axis equals to the current row index, print out "*" to mark this position
                            long positionOfThisGroup = ((resultOfThisGroup - summaryResults.first()) * (NUM_OF_ROWS - 2)) / (summaryResults.last() - summaryResults.first()) + 1;
                            if (positionOfThisGroup == (long) rowIndex) {
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
                    System.out.print(" ".repeat(maxLengthOfResult) + "\t " + " ".repeat(spaceBetweenLabelOnXaxis) + (i + 1));
                } else {
                    // If not, place it at the first column in the chart
                    System.out.print(" ".repeat(maxLengthOfResult) + "\t " + (i + 1));
                }
                continue;
            }

            // If the group number has more than 2 digits, place them appropriately
            if (Integer.toString(i).length() > 1) {
                System.out.print(" ".repeat(spaceBetweenLabelOnXaxis - Integer.toString(i).length() + 1) + (i + 1));
                continue;
            }

            // Place all of the group numbers with equal spaces between them
            System.out.print(" ".repeat(spaceBetweenLabelOnXaxis) + (i + 1));
        }

        System.out.println();
        System.out.println(" ".repeat(maxLengthOfResult + 1) + "\t" + " ".repeat(NUM_OF_COLS / 2 - 5) + "Group");

        // Print out the group number and the group name that it represents
        for (int i = 0; i < numOfGroups; i++) {
            System.out.printf("Group %d: %s\n", i + 1, groups.get(i));
        }
    }
}