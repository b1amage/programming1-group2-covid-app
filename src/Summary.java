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

    public LinkedHashMap<String, Integer> getGroupings() {
        return groupings;
    }

    public static Summary createSummary(Data data, String groupingOption, String metricOption, String resultOption, int dividingNumber) {
        return new Summary(data, groupingOption, metricOption, resultOption, dividingNumber);
    }

    public void processData() {
        boolean isVaccinatedData = false;
        GroupData groupData = new GroupData(data.getRowsFromStartDate());
        ArrayList<ArrayList<Row>> groupsOfDates;

        groupings = new LinkedHashMap<>();
        if (groupingMethod.equals("no grouping")) {
            groupData.noGrouping();

        } else if (groupingMethod.equals("number of groups")) {
            int numOfGroups = dividingNumber;
            groupData.groupDataByNumberOfGroups(numOfGroups);

        } else if (groupingMethod.equals("number of days")) {
            int numOfDays = dividingNumber;
            groupData.groupDataByNumberOfDays(numOfDays);
        }
        groupsOfDates = groupData.getGroupedData();
        if (groupsOfDates == null) {
            groupings = null;
            return;
        }

        MetricData metricData = new MetricData(groupsOfDates);
        ArrayList<ArrayList<Integer>> valuesOfEachRow;
        metricData.getValues(metricType);

        if (metricType.equals("people vaccinated")) {
            isVaccinatedData = true;
        }
        valuesOfEachRow = metricData.getValuesOfEachRow();

        ResultData resultData = new ResultData(data, valuesOfEachRow);
        ArrayList<Integer> valuesOfEachGroup;
        if (isVaccinatedData) {
            if (resultType.equals("new total")) {
                resultData.calculateByNewTotalForAccumulatedValue();
            } else if (resultType.equals("up to")) {
                resultData.calculateByUpToForAccumulatedValue();
            }
        } else {
            if (resultType.equals("new total")) {
                resultData.calculateByNewTotal();
            } else if (resultType.equals("up to")) {
                resultData.calculateByUpTo(metricType);
            }
        }

        valuesOfEachGroup = resultData.getValuesOfEachGroup();

        if (groupingMethod.equals("no grouping")) {
            for (int i = 0; i < groupsOfDates.size(); i++) {
                groupings.put(groupsOfDates.get(i).get(0).getDate(), valuesOfEachGroup.get(i));
            }
        } else {
            for (int i = 0; i < groupsOfDates.size(); i++) {
                int groupSize = groupsOfDates.get(i).size();
                groupings.put(groupsOfDates.get(i).get(0).getDate() + " - " + groupsOfDates.get(i).get(groupSize - 1).getDate(), valuesOfEachGroup.get(i));
            }
        }

        System.out.println(groupings);
    }
}

class GroupData {
    private ArrayList<Row> rawData;
    private ArrayList<ArrayList<Row>> groupedData;

    public GroupData(ArrayList<Row> rawData) {
        setRawData(rawData);
    }

    public void setRawData(ArrayList<Row> rawData) {
        this.rawData = rawData;
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
        int groupIndexToIncreaseSize = rawData.size() / numOfGroups;
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

        if (rawData.size() % numOfDays != 0) {
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
    private ArrayList<ArrayList<Integer>> valuesOfEachRow = new ArrayList<>();

    public MetricData(ArrayList<ArrayList<Row>> groupedData) {
        setGroupedData(groupedData);
    }

    public void setGroupedData(ArrayList<ArrayList<Row>> groupedData) {
        this.groupedData = groupedData;
    }

    public void getValues(String metricOption) {
        for (int i = 0; i < groupedData.size(); i++) {
            valuesOfEachRow.add(new ArrayList<>());
            for (Row row : groupedData.get(i)) {
                if (metricOption.equals("positive cases")) {
                    valuesOfEachRow.get(i).add(row.getNewCases());
                } else if (metricOption.equals("new deaths")) {
                    valuesOfEachRow.get(i).add(row.getNewDeaths());
                } else if (metricOption.equals("people vaccinated")){
                    valuesOfEachRow.get(i).add(row.getPeopleVaccinated());
                }
            }
        }
    }

    public ArrayList<ArrayList<Integer>> getValuesOfEachRow() {
        return valuesOfEachRow;
    }
}

class ResultData {
    private Data data;
    private String metricOption;
    private ArrayList<ArrayList<Integer>> valuesOfEachRow;
    private ArrayList<Integer> valuesOfEachGroup = new ArrayList<>();

    public ResultData(Data data, ArrayList<ArrayList<Integer>> valuesOfEachRow) {
        setData(data);
        setValuesOfEachRow(valuesOfEachRow);
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setValuesOfEachRow(ArrayList<ArrayList<Integer>> valuesOfEachRow) {
        this.valuesOfEachRow = valuesOfEachRow;
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

    public void calculateByUpTo(String metricOption) {
        int upToValue = 0;
        String firstDateOfFirstGroup = data.getRowsFromStartDate().get(0).getDate();
        ArrayList<Row> rowsFromCSV = data.getRows();
        for (Row row : rowsFromCSV) {
            if (row.getLocation().equals(data.getLocation())) {
                if (row.getDate().equals(firstDateOfFirstGroup)) {break;}
                if (metricOption.equals("positive cases")) {
                    upToValue += row.getNewCases();
                }
                if (metricOption.equals("new deaths")) {
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

    public ArrayList<Integer> getValuesOfEachGroup() {
        return valuesOfEachGroup;
    }
}
