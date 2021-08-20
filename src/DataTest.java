import java.io.IOException;

public class DataTest {
    public static void main(String[] args) throws IOException {
        UserInterface userInterface = new UserInterface();
        userInterface.displayUI();
        Data data = Data.createData1();
        data.createRowData();
        DataProcess.displayRows(data.getRowsFromStartDate());
    }
}
