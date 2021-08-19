import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;


public class UserInterface {
    private ArrayList<Row> data;

    public UserInterface(){}

    public void setData(ArrayList<Row> data){
        this.data = data;
    }

    public ArrayList<Row> getData(){
        return this.data;
    }

    public void displayUI() throws IOException {
        boolean isRunning = true;
        Scanner sc = new Scanner(System.in);
        while (isRunning){
            Data data = Data.createData();
            data.createRowData();
            DataProcess.displayRows(data.getRowsFromStartDate());
            System.out.println("(1) Chart display \n (2) Tabular display");
            int displayMethodOption = Integer.parseInt(sc.nextLine());

            if (displayMethodOption == 1){
                //Chart display
            } else if (displayMethodOption == 2){
                //Tabular display
            };

            // Ask the user if they want to continue
            System.out.println("===========================");
            System.out.println("Do you wish to continue \n (1) Yes \n (2) No");
            int cont = Integer.parseInt(sc.nextLine());
            setData(data.getRowsFromStartDate());

            // If user choose 1 then contimue, if 2 then break and stop the cycle
            isRunning = (1 == cont);
        }
    }
}
