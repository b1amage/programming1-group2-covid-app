import java.util.ArrayList;
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

        GroupData groupData = new GroupData(data.getRowsFromStartDate(), groupingMethod, dividingNumber);
        groupData.createGroupData();

        ArrayList<ArrayList<Row>> groupsOfDates;
        groupsOfDates = groupData.getGroupedData();
        if (groupsOfDates == null) {
            groupings = null;
            return;
        }

        MetricData metricData = new MetricData(groupsOfDates, metricType);
        metricData.createMetricData();
        ArrayList<ArrayList<Integer>> valuesOfEachRow;

        valuesOfEachRow = metricData.getValuesOfEachRow();

        ResultData resultData;

        if (metricType.equals("people vaccinated")) {
            resultData = new ResultDataForAccumulateValue(getData(), resultType, metricType, valuesOfEachRow);
        } else {
            resultData = new ResultData(getData(), resultType, metricType, valuesOfEachRow);
        }

        resultData.createResultData();
        ArrayList<Integer> valuesOfEachGroup;

        valuesOfEachGroup = resultData.getValuesOfEachGroup();

        for (int i = 0; i < groupsOfDates.size(); i++) {
            int groupSize = groupsOfDates.get(i).size();
            if (groupSize == 1) {
                groupings.put(groupsOfDates.get(i).get(0).getDate(), valuesOfEachGroup.get(i));
                continue;
            }
            groupings.put(groupsOfDates.get(i).get(0).getDate() + " - " + groupsOfDates.get(i).get(groupSize - 1).getDate(), valuesOfEachGroup.get(i));
        }
    }
}

class GroupData {
    private ArrayList<Row> rawData;
    private ArrayList<ArrayList<Row>> groupedData;
    private String groupingMethod;
    private int dividingNumber;

    public GroupData(ArrayList<Row> rawData, String groupingMethod, int dividingNumber) {
        setRawData(rawData);
        setGroupingMethod(groupingMethod);
        setDividingNumber(dividingNumber);
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
        groupedData = new ArrayList<>();
        int groupIndex = 0;
        for (Row row : rawData) {
            groupedData.add(new ArrayList<>());
            groupedData.get(groupIndex).add(row);
            groupIndex++;
        }
    }

    public void groupDataByNumberOfGroups(int numOfGroups) {
        groupedData = new ArrayList<>();
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

            groupedData.add(new ArrayList<>());
            for (int step = 0; step < numOfRows; step++) {
                groupedData.get(groupIndex).add(rawData.get(i + step));
            }

            groupIndex++;
        }
    }

    public void groupDataByNumberOfDays(int numOfDays) {
        groupedData = new ArrayList<>();
        int groupIndex = 0;

        if (rawData.size() % numOfDays != 0 || rawData.size() == numOfDays) {
            System.out.println("=========");
            System.out.println("Cannot divide the data into that number of days");
            groupedData = null;
            return;
        }

        for (int i = 0; i < rawData.size(); i+=numOfDays) {
            groupedData.add(new ArrayList<>());
            for (int step = 0; step < numOfDays; step++) {
                groupedData.get(groupIndex).add(rawData.get(i + step));
            }

            groupIndex++;
        }
    }

    public ArrayList<ArrayList<Row>> getGroupedData() {
        return groupedData;
    }
}
class MetricData {
    private ArrayList<ArrayList<Row>> groupedData;
    private String metricType;
    private ArrayList<ArrayList<Integer>> valuesOfEachRow;

    public MetricData(ArrayList<ArrayList<Row>> groupedData, String metricType) {
        setGroupedData(groupedData);
        setMetricType(metricType);
    }

    public void setGroupedData(ArrayList<ArrayList<Row>> groupedData) {
        this.groupedData = groupedData;
    }

    public void setMetricType(String metricType) {
        this.metricType = metricType;
    }

    public void createMetricData() {
        valuesOfEachRow = new ArrayList<>();
        for (int i = 0; i < groupedData.size(); i++) {
            valuesOfEachRow.add(new ArrayList<>());
            for (Row row : groupedData.get(i)) {
                switch (metricType) {
                    case "positive cases":
                        valuesOfEachRow.get(i).add(row.getNewCases());
                        break;
                    case "new deaths":
                        valuesOfEachRow.get(i).add(row.getNewDeaths());
                        break;
                    case "people vaccinated":
                        valuesOfEachRow.get(i).add(row.getPeopleVaccinated());
                        break;
                }
            }
        }
    }

    public ArrayList<ArrayList<Integer>> getValuesOfEachRow() {
        return valuesOfEachRow;
    }
}

class ResultData {
    protected Data data;
    protected String resultType;
    protected String metricType;
    protected ArrayList<ArrayList<Integer>> valuesOfEachRow;
    protected  ArrayList<Integer> valuesOfEachGroup = new ArrayList<>();

    public ResultData(Data data, String resultType, String metricType, ArrayList<ArrayList<Integer>> valuesOfEachRow) {
        setData(data);
        setResultType(resultType);
        setMetricType(metricType);
        setValuesOfEachRow(valuesOfEachRow);
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

    public void setValuesOfEachRow(ArrayList<ArrayList<Integer>> valuesOfEachRow) {
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
        for (ArrayList<Integer> group : valuesOfEachRow) {
            int total = 0;
            for (int value : group) {
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
            }
        }
        for (ArrayList<Integer> group : valuesOfEachRow) {
            for (int value : group) {
                upToValue += value;
            }
            valuesOfEachGroup.add(upToValue);
        }
    }

    public ArrayList<Integer> getValuesOfEachGroup() {
        return valuesOfEachGroup;
    }
}

class ResultDataForAccumulateValue extends ResultData{
    public ResultDataForAccumulateValue(Data data, String resultType, String metricType, ArrayList<ArrayList<Integer>> valuesOfEachRow) {
        super(data, resultType, metricType, valuesOfEachRow);
    }

    public void createResultData() {
        if (metricType.equals("people vaccinated")) {
            if (resultType.equals("new total")) {
                calculateByNewTotalForAccumulatedValue();
            }

            if (resultType.equals("up to")) {
                calculateByUpToForAccumulatedValue();
            }
        }
    }

    public void calculateByNewTotalForAccumulatedValue() {
        for (ArrayList<Integer> group : valuesOfEachRow) {
            valuesOfEachGroup.add(group.get(group.size() - 1) - group.get(0));
        }
    }

    public void calculateByUpToForAccumulatedValue() {
        for (ArrayList<Integer> group : valuesOfEachRow) {
            valuesOfEachGroup.add(group.get(group.size() - 1));
        }
    }
}
