import java.io.IOException;

public class DataTest {
    public static void main(String[] args) throws IOException {
        Data data = Data.createData();
        data.createRowData();
        DataProcess.displayRows(data.getRowsFromStartDate());
    }
}
