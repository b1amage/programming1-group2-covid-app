import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<Row> rows = DataProcess.createRowList();

        DataProcess.displayRows(rows);
    }
}
