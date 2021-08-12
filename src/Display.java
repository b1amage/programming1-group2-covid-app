import java.io.IOException;

public class Display {
    public static void tabularDisplay(Data data) {
        System.out.println("Range" + "\t\t\t\t" + "Value");

        for (Row row : data.getRowsFromStartDate()) {
            System.out.println(row.getDate() + "\t\t\t" + row.getNewCases());
        }
    }

    public static void chartDisplay(Data data) {
        char[][] chart = new char[24][80];

    }

    public static void main(String[] args) throws IOException {
        Data data = Data.createData();
        data.createRowData();
        tabularDisplay(data);
    }
}
