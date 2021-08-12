import java.io.FileNotFoundException;

public class DataTest {
    public static void main(String[] args) {
        Data data = Data.createData();
        data.createRowData();
        DataProcess.displayRows(data.getRowsFromStartDate());
    }
}
