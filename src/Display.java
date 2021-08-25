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

    public void tabularDisplay(Summary summary) {
        System.out.printf("\n%s\n", "TABULAR DISPLAY");
        int maxLengthOfGroupName = 0;
        for (String groupName : summary.getGroupings().keySet()) {
            if (groupName.length() > maxLengthOfGroupName) {
                maxLengthOfGroupName = groupName.length();
            }
        }
        System.out.println("Range" + " ".repeat(maxLengthOfGroupName) + "Value");

        for (String groupName : summary.getGroupings().keySet()) {
            System.out.println(groupName + " ".repeat(5 + (maxLengthOfGroupName - groupName.length())) + summary.getGroupings().get(groupName));
        }
    }

    public void chartDisplay(Summary summary) {
        int numOfRows = 24;
        int numOfCols = 80;
        LinkedHashMap<String, Integer> summaryData = summary.getGroupings();
        int numOfGroups = summaryData.size();

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

        SortedSet<Integer> summaryResults = new TreeSet<>();
        for (String groupName : summaryData.keySet()) {
            summaryResults.add(summaryData.get(groupName));
        }

        if (numOfGroups == 0 || numOfGroups > 26 || summaryResults.last() <= 0) {
            System.out.println("=========");
            System.out.println("There is no data or the data is to large to display by chart");
            display = null;
            return;
        }

        if (numOfGroups == 1) {
            System.out.println("=========");
            System.out.println("You should use tabular display to show the data of only one group");
            display = null;
            return;
        }

        ArrayList<String> groups = new ArrayList<>(summaryData.keySet());

        int indentation = Integer.toString(summaryResults.last()).length();
        int positionOfLabelOnY_axis = 0;

        int spaceBetweenLabelOnX_axis = (numOfCols - numOfGroups - 1) / (numOfGroups - 1);

        // Map the position on X axis to a result on Y axis
        LinkedHashMap<Integer, Integer> mapPositionOnXtoGroup = new LinkedHashMap<>();
        int index = 0;
        for (String groupName : summaryData.keySet()) {
            mapPositionOnXtoGroup.put((spaceBetweenLabelOnX_axis + 1) * index + 1, summaryData.get(groupName));
            index++;
        }

        // Store summary results in LinkedList to iterate backward.
        LinkedList<Integer> results = new LinkedList<>(summaryResults);

        System.out.printf("\n%s\n", "CHART DISPLAY");
        System.out.println(" ".repeat(indentation) + "\t" + valueName);
        for (int i = numOfRows - 1; i >= 0; i--) {
            int resultOnThisRow = 0;
            Iterator<Integer> iterator = results.descendingIterator();
            while (iterator.hasNext()) {
                int result = iterator.next();
                if (summaryResults.first().equals(summaryResults.last())) {
                    positionOfLabelOnY_axis = 1;
                } else {
                    positionOfLabelOnY_axis = ((result - summaryResults.first()) * (numOfRows - 2)) / (summaryResults.last() - summaryResults.first()) + 1;
                }

                if (i == positionOfLabelOnY_axis) {
                    resultOnThisRow = result;
                    System.out.print(result + " ".repeat(indentation - Integer.toString(result).length()) + "\t|");
                    results.removeLast();
                    break;
                }
            }

            for (int j = 0; j < numOfCols; j++) {
                if (j == 0) {
                    if (i != positionOfLabelOnY_axis) {
                        System.out.print(" ".repeat(indentation) + "\t|");
                    }
                    continue;
                }

                if (i == 0) {
                    System.out.print('_');
                    continue;
                }

                if (i == positionOfLabelOnY_axis) {
                    if (mapPositionOnXtoGroup.containsKey(j)) {
                        int resultAtThisGroup = mapPositionOnXtoGroup.get(j);
                        if (resultAtThisGroup == resultOnThisRow) {
                            System.out.print('*');
                            continue;
                        } else {
                            int positionOfThisGroup = ((resultAtThisGroup - summaryResults.first()) * (numOfRows - 2)) / (summaryResults.last() - summaryResults.first()) + 1;
                            if (positionOfThisGroup == i) {
                                System.out.print('*');
                                continue;
                            }
                        }
                    }
                }

                System.out.print(' ');
            }

            System.out.println();
        }

        for (int i = 0; i < numOfGroups; i++) {
            if (i == 0) {
                System.out.print(" ".repeat(indentation) + "\t " + (i + 1));
                continue;
            }

            if (Integer.toString(i).length() > 1) {
                System.out.print(" ".repeat(spaceBetweenLabelOnX_axis - Integer.toString(i).length() + 1) + (i + 1));
                continue;
            }
            System.out.print(" ".repeat(spaceBetweenLabelOnX_axis) + (i + 1));
        }

        System.out.println("\n" + " ".repeat(indentation + 1) + "\t" + " ".repeat(numOfCols / 2 - 5) + "Group");

        for (int i = 0; i < numOfGroups; i++) {
            System.out.printf("Group %d: %s\n", i + 1, groups.get(i));
        }
    }
}
