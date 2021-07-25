import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here
        Scanner sc = new Scanner(new File("src/data.csv"));
        ArrayList<String> dataByRow = new ArrayList<String>();

        while (sc.hasNextLine()) {
            dataByRow.add(sc.nextLine());
        }

        printArrayList(dataByRow);
        System.out.println(dataByRow.get(0));
    }

    public static void printArrayList(ArrayList<String> arrayList) {
        for (String row : arrayList) {
            System.out.println(row);
        }
    }


}
