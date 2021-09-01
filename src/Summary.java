/*
  Class: Summary
  Purpose: Used to process data so it can be ready to display
  Contributors: Kha Tuan, Anh Duy
  Created date: 25/7/2021
  Last modified: 26/8/2021
  Version 1.0
 */
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Summary {
    private ArrayList<Row> rowsFromFile;
    private ArrayList<Row> rawData;
    private String location;
    private String groupingMethod;
    private String metricType;
    private String resultType;
    private int dividingNumber;
    private LinkedHashMap<String, Integer> groupings;

    public Summary(ArrayList<Row> rawData, String groupingMethod, int dividingNumber) {
        setRawData(rawData);
        setGroupingMethod(groupingMethod);
        setDividingNumber(dividingNumber);
    }

    public Summary(ArrayList<Row> rowsFromFile, ArrayList<Row> rawData, String location, String groupingMethod, String metricType, String resultType, int dividingNumber) {
        setRowsFromFile(rowsFromFile);
        setRawData(rawData);
        setLocation(location);
        setGroupingMethod(groupingMethod);
        setMetricType(metricType);
        setResultType(resultType);
        setDividingNumber(dividingNumber);
    }

    public void setRowsFromFile(ArrayList<Row> rowsFromFile) {
        this.rowsFromFile = rowsFromFile;
    }

    public void setRawData(ArrayList<Row> rawData) {
        this.rawData = rawData;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setGroupingMethod(String groupingMethod) {
        this.groupingMethod = groupingMethod;
    }

    public void setMetricType(String metricType) {
        this.metricType = metricType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public void setDividingNumber(int dividingNumber) {
        this.dividingNumber = dividingNumber;
    }

    public String getMetricType() {
        return metricType;
    }

    public LinkedHashMap<String, Integer> getGroupings() {
        return groupings;
    }

    public static Summary createTempSummary(ArrayList<Row> rawData, String groupingOption, int dividingNumber) {
        return new Summary(rawData, groupingOption, dividingNumber);
    }

    public static Summary createSummary(ArrayList<Row> rowsFromFile, ArrayList<Row> rawData,String location, String groupingOption, String metricOption, String resultOption, int dividingNumber) {
        return new Summary(rowsFromFile, rawData, location, groupingOption, metricOption, resultOption, dividingNumber);
    }

    /**
     * This method generates values for the LinkedHashMap to store the appropriate data that user have input
     * The keys will be the group name which is categorized from the groupingMethod
     * The values will be the data from the metricType & resultType
     */
    public void processData() {
        groupings = new LinkedHashMap<>();

        GroupData groupData = new GroupData(rawData, groupingMethod, dividingNumber);
        groupData.createGroupData();
        ArrayList<Group> groupsOfDates;
        groupsOfDates = groupData.getGroupedData();
        if (groupsOfDates == null) {
            groupings = null;
            return;
        }

        MetricData metricData = new MetricData(groupsOfDates, metricType);
        metricData.createMetricData();
        ArrayList<GroupValue> valuesOfEachRow;
        valuesOfEachRow = metricData.getValuesOfEachRow();


        ResultData resultData = new ResultData(rowsFromFile, rawData, location, resultType, metricType, valuesOfEachRow);
        resultData.createResultData();
        ArrayList<Integer> valuesOfEachGroup;
        valuesOfEachGroup = resultData.getValuesOfEachGroup();

        int groupIndex = 0;
        for (Group group : groupsOfDates) {
            int groupSize = group.getDataPerGroup().size();
            if (groupSize == 1) {
                groupings.put(group.getDataPerGroup().get(0).getDate(), valuesOfEachGroup.get(groupIndex));
                groupIndex++;
                continue;
            }

            groupings.put(group.getDataPerGroup().get(0).getDate() + " - " + group.getDataPerGroup().get(groupSize - 1).getDate(), valuesOfEachGroup.get(groupIndex));
            groupIndex++;
        }
    }

    /**
     * This method checks if the grouping method is valid
     * @return a boolean to check if the data can be grouped or not
     */
    public boolean isValidGroupingMethod() {
        GroupData groupData = new GroupData(rawData, groupingMethod, dividingNumber);
        groupData.createGroupData();
        ArrayList<Group> groupsOfDates;
        groupsOfDates = groupData.getGroupedData();
        return groupsOfDates != null;
    }

}
class Group {
    private final ArrayList<Row> dataPerGroup;

    public Group() {
        dataPerGroup = new ArrayList<>();
    }

    public void addRow(Row row) {
        dataPerGroup.add(row);
    }

    public ArrayList<Row> getDataPerGroup() {
        return dataPerGroup;
    }
}

class GroupData {
    private ArrayList<Row> rawData;
    private ArrayList<Group> groupedData;
    private String groupingMethod;
    private int dividingNumber;

    public GroupData(ArrayList<Row> rawData, String groupingMethod, int dividingNumber) {
        setRawData(rawData);
        setGroupingMethod(groupingMethod);
        setDividingNumber(dividingNumber);
        groupedData = new ArrayList<>();
    }


    public void setRawData(ArrayList<Row> rawData) {
        this.rawData = rawData;
    }

    public void setGroupingMethod(String groupingMethod) {
        this.groupingMethod = groupingMethod;
    }

    public void setDividingNumber(int dividingNumber) {
        this.dividingNumber = dividingNumber;
    }

    /**
     * This function create groups data based on the groupingMethod
     */
    public void createGroupData() {
        switch (groupingMethod) {
            case "no grouping":
                noGrouping();
                break;
            case "number of groups":
                int numOfGroups = dividingNumber;
                groupDataByNumberOfGroups(numOfGroups);
                break;
            case "number of days":
                int numOfDays = dividingNumber;
                groupDataByNumberOfDays(numOfDays);
                break;
        }
    }

    /**
     * This function generates the groups based on noGrouping Option
     */
    public void noGrouping() {
        for (Row row : rawData) {
            Group group = new Group();
            group.addRow(row);
            groupedData.add(group);
        }
    }

    /**
     * This function generates the groups based on NumberOfGroups Option
     */
    public void groupDataByNumberOfGroups(int numOfGroups) {
        if (numOfGroups <= 0) {
            System.out.println("=========");
            System.out.println("The number of groups must be greater than zero");
            groupedData = null;
            return;
        }

        int numOfRows = rawData.size() / numOfGroups;
        int groupIndexToIncreaseSize = numOfGroups;
        int groupIndex = 0;

        if (numOfGroups > rawData.size()) {
            System.out.println("=========");
            System.out.println("The number of groups is bigger than the number of days in the data");
            groupedData = null;
            return;
        }

        if (rawData.size() % numOfGroups != 0) {
            groupIndexToIncreaseSize = numOfGroups - (rawData.size() % numOfGroups);
        }

        for (int i = 0; i < rawData.size(); i+=numOfRows) {
            if (groupIndex == groupIndexToIncreaseSize) {
                numOfRows++;
            }

            Group group = new Group();
            for (int step = 0; step < numOfRows; step++) {
                group.addRow(rawData.get(i + step));
            }
            groupedData.add(group);

            groupIndex++;
        }
    }

    /**
     * This function generates the groups based on NumberOfDays Option
     */
    public void groupDataByNumberOfDays(int numOfDays) {
        if (numOfDays <= 0) {
            System.out.println("=========");
            System.out.println("The number of days must be greater than zero");
            groupedData = null;
            return;
        }

        if (rawData.size() % numOfDays != 0) {
            System.out.println("=========");
            System.out.println("Cannot divide the data into that number of days");
            groupedData = null;
            return;
        }

        for (int i = 0; i < rawData.size(); i+=numOfDays) {
            Group group = new Group();
            for (int step = 0; step < numOfDays; step++) {
                group.addRow(rawData.get(i + step));
            }
            groupedData.add(group);
        }
    }

    public ArrayList<Group> getGroupedData() {
        return groupedData;
    }
}

class GroupValue{
    private final ArrayList<Integer> groupValue;

    public GroupValue() {
        groupValue = new ArrayList<>();
    }

    public void addValue(int value) {
        groupValue.add(value);
    }

    public ArrayList<Integer> getGroupValue() {
        return groupValue;
    }

    public int getTotalValue() {
        int total = 0;
        for (int value : getGroupValue()) {
            total += value;
        }

        return total;
    }
}

class MetricData {
    private ArrayList<Group> groupedData;
    private String metricType;
    private final ArrayList<GroupValue> valuesOfEachRow;

    public MetricData(ArrayList<Group> groupedData, String metricType) {
        setGroupedData(groupedData);
        setMetricType(metricType);
        valuesOfEachRow = new ArrayList<>();
    }

    public void setGroupedData(ArrayList<Group> groupedData) {
        this.groupedData = groupedData;
    }

    public void setMetricType(String metricType) {
        this.metricType = metricType;
    }

    /**
     * This function get the metric data based on metricOption
     * @param metricOption metric type that user have input
     * @param row row in each group
     */
    public int getMetricData(String metricOption, Row row) {
        switch (metricOption) {
            case "positive cases":
                return row.getNewCases();
            case "new deaths":
                return row.getNewDeaths();
            case "people vaccinated":
                return row.getPeopleVaccinated();
            default:
                return 0;
        }
    }

    /**
     * This function gets all the rows value based on the metricType for each group
     */
    public void createMetricData() {
        for (Group group : groupedData) {
            GroupValue groupValue = new GroupValue();
            for (Row row : group.getDataPerGroup()) {
                groupValue.addValue(getMetricData(metricType, row));
            }

            valuesOfEachRow.add(groupValue);
        }
    }

    public ArrayList<GroupValue> getValuesOfEachRow() {
        return valuesOfEachRow;
    }
}

class ResultData {
    private ArrayList<Row> rowsFromFile;
    private ArrayList<Row> rawData;
    private String location;
    private String resultType;
    private String metricType;
    private ArrayList<GroupValue> valuesOfEachRow;
    private final ArrayList<Integer> valuesOfEachGroup;

    public ResultData(ArrayList<Row> rowsFromFile, ArrayList<Row> rawData, String location, String resultType, String metricType, ArrayList<GroupValue> valuesOfEachRow) {
        setRowsFromFile(rowsFromFile);
        setRawData(rawData);
        setLocation(location);
        setResultType(resultType);
        setMetricType(metricType);
        setValuesOfEachRow(valuesOfEachRow);
        valuesOfEachGroup = new ArrayList<>();
    }

    public void setRowsFromFile(ArrayList<Row> rowsFromFile) {
        this.rowsFromFile = rowsFromFile;
    }

    public void setRawData(ArrayList<Row> rawData) {
        this.rawData = rawData;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public void setMetricType(String metricType) {
        this.metricType = metricType;
    }

    public void setValuesOfEachRow(ArrayList<GroupValue> valuesOfEachRow) {
        this.valuesOfEachRow = valuesOfEachRow;
    }

    public void createResultData() {
        if (resultType.equals("new total")) {
            calculateByNewTotal();
        } else if (resultType.equals("up to")) {
            calculateByUpTo();
        }
    }

    /**
     * This function calculates the NewTotal value for each group
     */
    public void calculateByNewTotal() {
        for (GroupValue groupValue : valuesOfEachRow) {
            valuesOfEachGroup.add(groupValue.getTotalValue());
        }
    }

    /**
     * This function calculates the UpTo value for each group
     */
    public void calculateByUpTo() {
        int upToValue = 0;
        String firstDateOfFirstGroup = rawData.get(0).getDate();
        for (Row row : rowsFromFile) {
            if (row.getLocation().equals(location)) {
                if (row.getDate().equals(firstDateOfFirstGroup)) {break;}
                if (metricType.equals("positive cases")) {
                    upToValue += row.getNewCases();
                }
                if (metricType.equals("new deaths")) {
                    upToValue += row.getNewDeaths();
                }
                if (metricType.equals("people vaccinated")){
                    upToValue += row.getPeopleVaccinated();
                }
            }
        }
        for (GroupValue groupValue : valuesOfEachRow) {
            upToValue += groupValue.getTotalValue();
            valuesOfEachGroup.add(upToValue);
        }
    }

    public ArrayList<Integer> getValuesOfEachGroup() {
        return valuesOfEachGroup;
    }
}
