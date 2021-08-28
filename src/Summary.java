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
    private Data data;
    private String groupingMethod;
    private String metricType;
    private String resultType;
    private int dividingNumber;
    private LinkedHashMap<String, Integer> groupings;

    public Summary(Data data, String groupingMethod, int dividingNumber) {
        setData(data);
        setGroupingMethod(groupingMethod);
        setDividingNumber(dividingNumber);
    }

    public Summary(Data data, String groupingMethod, String metricType, String resultType, int dividingNumber) {
        setData(data);
        setGroupingMethod(groupingMethod);
        setMetricType(metricType);
        setResultType(resultType);
        setDividingNumber(dividingNumber);
    }

    public void setData(Data data) {
        this.data = data;
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

    public Data getData() {
        return data;
    }

    public String getMetricType() {
        return metricType;
    }

    public LinkedHashMap<String, Integer> getGroupings() {
        return groupings;
    }

    public static Summary createTempSummary(Data data, String groupingOption, int dividingNumber) {
        return new Summary(data, groupingOption, dividingNumber);
    }

    public static Summary createSummary(Data data, String groupingOption, String metricOption, String resultOption, int dividingNumber) {
        return new Summary(data, groupingOption, metricOption, resultOption, dividingNumber);
    }

    /**
     * This method generates values for the LinkedHashMap to store the appropriate data that user have input
     * The keys will be the group name which is categorized from the groupingMethod
     * The values will be the data from the metricType & resultType
     */
    public void processData() {
        groupings = new LinkedHashMap<>();

        GroupData groupData = new GroupData(getData().getRowsFromStartDate(), groupingMethod, dividingNumber);
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


        ResultData resultData = new ResultData(getData(), resultType, metricType, valuesOfEachRow);
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
        GroupData groupData = new GroupData(getData().getRowsFromStartDate(), groupingMethod, dividingNumber);
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
    protected Data data;
    protected String resultType;
    protected String metricType;
    protected ArrayList<GroupValue> valuesOfEachRow;
    protected ArrayList<Integer> valuesOfEachGroup;

    public ResultData(Data data, String resultType, String metricType, ArrayList<GroupValue> valuesOfEachRow) {
        setData(data);
        setResultType(resultType);
        setMetricType(metricType);
        setValuesOfEachRow(valuesOfEachRow);
        valuesOfEachGroup = new ArrayList<>();
    }

    public void setData(Data data) {
        this.data = data;
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
        String firstDateOfFirstGroup = data.getRowsFromStartDate().get(0).getDate();
        ArrayList<Row> rowsFromCSV = data.getRows();
        for (Row row : rowsFromCSV) {
            if (row.getLocation().equals(data.getLocation())) {
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
