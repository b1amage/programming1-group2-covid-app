import java.io.FileNotFoundException;

public class Test {
    public static void main(String[] args) throws FileNotFoundException {
        Data data = Data.createData();
//        data.display();
        data.createRowData();
        DataProcess.displayRows(data.getRowsFromStartDate());
//        DataProcess.displayRows(data.getRows());
    }
}
