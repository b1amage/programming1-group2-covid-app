import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class Summary {
    private Data data;
    private String groupingMethod;
    private String metricType;
    private String resultType;
    private int dividingNumber;
    private LinkedHashMap<String, Integer> groupings;

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

    public static Summary createSummary(Data data, String groupingOption, String metricOption, String resultOption, int dividingNumber) {
        return new Summary(data, groupingOption, metricOption, resultOption, dividingNumber);
    }

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
                continue;
            }

            groupings.put(group.getDataPerGroup().get(0).getDate() + " - " + group.getDataPerGroup().get(groupSize - 1).getDate(), valuesOfEachGroup.get(groupIndex));
            groupIndex++;
        }
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

    public void noGrouping() {
        for (Row row : rawData) {
            Group group = new Group();
            group.addRow(row);
        }
    }

    public void groupDataByNumberOfGroups(int numOfGroups) {
        int numOfRows = rawData.size() / numOfGroups;
        int groupIndexToIncreaseSize = numOfGroups;
        int groupIndex = 0;

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

    public void groupDataByNumberOfDays(int numOfDays) {
        if (rawData.size() % numOfDays != 0 || rawData.size() == numOfDays) {
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
}

class MetricData {
    private ArrayList<Group> groupedData;
    private String metricType;
    private ArrayList<GroupValue> valuesOfEachRow;

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

    public GroupValue getMetricData(String metricOption, Row row) {
        GroupValue groupValue = new GroupValue();
        switch (metricOption) {
            case "positive cases":
                groupValue.addValue(row.getNewCases());
                break;
            case "new deaths":
                groupValue.addValue(row.getNewDeaths());
                break;
            case "people vaccinated":
                groupValue.addValue(row.getPeopleVaccinated());
                break;
        }

        return groupValue;
    }

    public void createMetricData() {
        for (Group group : groupedData) {
            for (Row row : group.getDataPerGroup()) {
                valuesOfEachRow.add(getMetricData(metricType, row));
            }
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

    public void calculateByNewTotal() {
        for (GroupValue groupValue : valuesOfEachRow) {
            int total = 0;
            for (int value : groupValue.getGroupValue()) {
                total += value;
            }
            valuesOfEachGroup.add(total);
        }
    }

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
            for (int value : groupValue.getGroupValue()) {
                upToValue += value;
            }
            valuesOfEachGroup.add(upToValue);
        }
    }

    public ArrayList<Integer> getValuesOfEachGroup() {
        return valuesOfEachGroup;
    }
}
