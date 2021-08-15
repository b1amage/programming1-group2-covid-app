import java.util.ArrayList;
import java.io.IOException;
import java.util.Scanner;

public class Summary {

    public static ArrayList<Row> summaryData() throws IOException {
        Data data = Data.createData();
        data.createRowData();
        return data.getRowsFromStartDate();
    }

    public static void getInput() {

    }

    public static void grouping(ArrayList<Row> sumData) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your grouping method option:\n" +
                "(1) No grouping \n" + "(2) Number of groups \n" + "(3) Number of Days");
        int method = Integer.parseInt(sc.nextLine());
        if (method == 1) {
            // No grouping method, takes sumData as parameter
        }

    }


    public static void main(String[] args) {

    }
}
