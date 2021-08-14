import java.io.IOException;
import java.util.*;

public class Display {
    public static void tabularDisplay(Data data) {
        System.out.printf("\n%s\n", "TABULAR DISPLAY");
        System.out.println("Range" + "\t\t\t\t" + "Value");

        for (Row row : data.getRowsFromStartDate()) {
            System.out.println(row.getDate() + "\t\t\t" + row.getNewCases());
        }
    }

    public static void chartDisplay(Data data) {
        int numOfRows = 24;
        int numOfCols = 80;
        int numOfGroups = data.getRowsFromStartDate().size();

        String valueName = "New Cases";

        if (numOfGroups == 0 || numOfGroups > 26) {
            System.out.println("Cannot display this data");
            return;
        }

        SortedSet<Integer> summaryResults = new TreeSet<>();
        for (int i = 0; i < numOfGroups; i++) {
            summaryResults.add(data.getRowsFromStartDate().get(i).getNewCases());
        }

        String[] groups = new String[numOfGroups];
        for (int i = 0; i < numOfGroups; i++) {
            groups[i] = data.getRowsFromStartDate().get(i).getDate();
        }

        int indentation = Integer.toString(summaryResults.last()).length();
//        int unit = (summaryResults.last() - summaryResults.first()) / (numOfRows - 1);
        int positionOfLabelOnY_axis = 0;

        int spaceBetweenLabelOnX_axis = (numOfCols - numOfGroups - 1) / (numOfGroups - 1);
        ArrayList<Integer> positionOfGroupOnX_axis = new ArrayList<>();
        for (int i = 0; i < numOfGroups; i++) {
            positionOfGroupOnX_axis.add((spaceBetweenLabelOnX_axis + 1) * i + 1);
        }

        // Map the position on X axis to a result on Y axis
        LinkedHashMap<Integer, Integer> mapPositionOnXtoResult = new LinkedHashMap<>();
        for (int i = 0; i < numOfGroups; i++) {
            mapPositionOnXtoResult.put(positionOfGroupOnX_axis.get(i), data.getRowsFromStartDate().get(i).getNewCases());
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
                positionOfLabelOnY_axis = ((result - summaryResults.first()) * (numOfRows - 2)) / (summaryResults.last() - summaryResults.first()) + 1;

                if (i == positionOfLabelOnY_axis) {
                    resultOnThisRow = result;
                    System.out.print(result + " ".repeat(indentation - Integer.toString(result).length()) + "\t|");
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
                    if (positionOfGroupOnX_axis.contains(j)) {
                        int resultAtThisGroup = mapPositionOnXtoResult.get(j);
                        if (resultAtThisGroup == resultOnThisRow) {
                            System.out.print('*');
                            continue;
                        } else {
                            int positionOfThisGroup;
                            positionOfThisGroup = ((resultAtThisGroup - summaryResults.first()) * (numOfRows - 2)) / (summaryResults.last() - summaryResults.first()) + 1;

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
            System.out.printf("Group %d: %s\n", i + 1, groups[i]);
        }

    }

    public static void main(String[] args) throws IOException {
        Data data = Data.createData();
        data.createRowData();
        tabularDisplay(data);
        chartDisplay(data);
    }
}
