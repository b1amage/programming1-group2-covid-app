import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Summary {
    private ArrayList<Row> data;
    private int groupingMethod;
    private int metricType;
    private int resultType;
    private int dividingNumber;
    private LinkedHashMap<String, Integer> groupings;

    public Summary(ArrayList<Row> data, int groupingMethod, int metricType, int resultType, int dividingNumber) {
        setData(data);
        setGroupingMethod(groupingMethod);
        setMetricType(metricType);
        setResultType(resultType);
        setDividingNumber(dividingNumber);
    }

    public void setData(ArrayList<Row> data) {
        this.data = data;
    }

    public void setGroupingMethod(int groupingMethod) {
        this.groupingMethod = groupingMethod;
    }

    public void setMetricType(int metricType) {
        this.metricType = metricType;
    }

    public void setResultType(int resultType) {
        this.resultType = resultType;
    }

    public void setDividingNumber(int dividingNumber) {
        this.dividingNumber = dividingNumber;
    }

    public LinkedHashMap<String, Integer> getGroupings() {
        return groupings;
    }

    public static Summary createSummary(ArrayList<Row> data, int groupingOption, int metricOption, int resultOption, int dividingNumber) {
        return new Summary(data, groupingOption, metricOption, resultOption, dividingNumber);
    }

    public void processData() {
        boolean isVaccinatedData = false;
        GroupData groupData = new GroupData(data);
        ArrayList<ArrayList<Row>> groupedData = new ArrayList<>();

        groupings = new LinkedHashMap<>();
        if (groupingMethod == 1) {
            groupData.noGrouping();

        } else if (groupingMethod == 2) {
            int numOfGroups = dividingNumber;
            groupData.groupDataByNumberOfGroups(numOfGroups);

        } else if (groupingMethod == 3) {
            int numOfDays = dividingNumber;
            groupData.groupDataByNumberOfDays(numOfDays);
        }
        groupedData = groupData.getGroupedData();

        MetricData metricData = new MetricData(groupedData);
        ArrayList<ArrayList<Integer>> valuesOfEachRow = new ArrayList<>();
        if (metricType == 1) {
            metricData.getCase();
        } else if (metricType == 2) {
            metricData.getDeaths();
        } else if (metricType == 3){
            metricData.getVaccinated();
            isVaccinatedData = true;
        }
        valuesOfEachRow = metricData.getValuesOfEachRow();

        ResultData resultData = new ResultData(valuesOfEachRow);
        ArrayList<Integer> valuesOfEachGroup = new ArrayList<>();
        if (isVaccinatedData) {
            resultData.calculateByUpTo();
        } else {
            if (resultType == 1) {
                resultData.calculateByNewTotal();
            } else if (resultType == 2) {
                resultData.calculateByUpTo();
            }
        }
        valuesOfEachGroup = resultData.getValuesOfEachGroup();

        System.out.println(groupings);
    }

    public static void main(String[] args) throws IOException {
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
    private ArrayList<ArrayList<Integer>> valuesOfEachRow;

    public MetricData(ArrayList<ArrayList<Row>> groupedData) {
        setGroupedData(groupedData);
    }

    public void setGroupedData(ArrayList<ArrayList<Row>> groupedData) {
        this.groupedData = groupedData;
    }

    public void getCase() {
        for (int i = 0; i < groupedData.size(); i++) {
            valuesOfEachRow.add(new ArrayList<>());
            for (Row row : groupedData.get(i)) {
                valuesOfEachRow.get(i).add(row.getNewCases());
            }
        }
    }
    public void getDeaths() {
        for (int i = 0; i < groupedData.size(); i++) {
            valuesOfEachRow.add(new ArrayList<>());
            for (Row row : groupedData.get(i)) {
                valuesOfEachRow.get(i).add(row.getNewDeaths());
            }
        }
    }
    public void getVaccinated() {
        for (int i = 0; i < groupedData.size(); i++) {
            valuesOfEachRow.add(new ArrayList<>());
            for (Row row : groupedData.get(i)) {
                valuesOfEachRow.get(i).add(row.getPeopleVaccinated());
            }
        }
    }

    public ArrayList<ArrayList<Integer>> getValuesOfEachRow() {
        return valuesOfEachRow;
    }
}

class ResultData {
    private ArrayList<ArrayList<Integer>> valuesOfEachRow;
    private ArrayList<Integer> valuesOfEachGroup;

    public ResultData(ArrayList<ArrayList<Integer>> valuesOfEachRow) {
        setValuesOfEachRow(valuesOfEachRow);
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

    public void calculateByUpTo() {

    }

    public ArrayList<Integer> getValuesOfEachGroup() {
        return valuesOfEachGroup;
    }
}
