import java.io.IOException;

public class User_interface {
    public static void main(String[] args) throws IOException {
        boolean isRunning = true;
        while (isRunning){
            Data data = Data.createData();
            data.createRowData();
            DataProcess.displayRows(data.getRowsFromStartDate());
        }
    }
}
