import java.io.IOException;
import java.util.Scanner;


public class User_interface {
    public static void main(String[] args) throws IOException {
        boolean isRunning = true;
        Scanner sc = new Scanner(System.in);
        while (isRunning){
            Data data = Data.createData();
            data.createRowData();
            DataProcess.displayRows(data.getRowsFromStartDate());
            System.out.println("Do you wish to conitnue \n (1) Yes \n (2) No");
            int cont = sc.nextInt();
            isRunning = (1 == cont);



        }
    }
}
