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
            System.out.println("(1) Chart display \n (2) Tabular display");
            int displayMethod = sc.nextInt();
            if (displayMethod == 1){
                //Chart display
            } else if (displayMethod == 2){
                //Tabular display
            };

            // Ask the user if they want to continue
            System.out.println("Do you wish to conitnue \n (1) Yes \n (2) No");
            int cont = sc.nextInt();
            isRunning = (1 == cont);



        }
    }
}
