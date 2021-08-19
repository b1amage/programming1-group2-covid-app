import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;


public class UserInterface {
    private ArrayList<Row> data;
    private int grouping_method;
    private int metric;
    private int result;
    private int display;


    //constructor
    public UserInterface(){}


    //getter setter
    public void setData(ArrayList<Row> data){
        this.data = data;
    }
    public ArrayList<Row> getData(){
        return this.data;
    }
    public void setGrouping_method(int grouping_method){ this.grouping_method = grouping_method; }
    public int getGrouping_method(){ return this.grouping_method; }
    public void setMetric(int metric){ this.metric = metric; }
    public int getMetric(){ return this.metric; }
    public void setResult(int result){ this.result = result; }
    public int getResult(){ return this.result; }
    public void setDisplay(int display){ this.display = display; }
    public int getDisplay(){ return this.display; }


    //display UI
    public void displayUI() throws IOException {
        boolean isRunning = true;
        Scanner sc = new Scanner(System.in);
        while (isRunning){
            Data data = Data.createData();
            data.createRowData();
            DataProcess.displayRows(data.getRowsFromStartDate());

            // Ask for summary method
            inputGroupingMethod();

           // Ask for display method
            displayMethod();

            // Ask the user if they want to continue
            System.out.println("===========================");
            System.out.println("Do you wish to continue \n (1) Yes \n (2) No");
            int cont = Integer.parseInt(sc.nextLine());
            setData(data.getRowsFromStartDate());

            // If user choose 1 then contimue, if 2 then break and stop the cycle
            isRunning = (1 == cont);
        }
    }

    // input grouping method
    public void inputGroupingMethod(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Choose your grouping method \n (1) No grouping \n (2) Number of groups \n (3) Number of days");
        int grouping = Integer.parseInt(sc.nextLine());
        setGrouping_method(grouping);
        System.out.println("Choose your metric \n (1) Positive cases \n (2) New deaths \n (3) People vaccinated");
        int metric = Integer.parseInt(sc.nextLine());
        setMetric(metric);
        System.out.println("Choose your result type \n (1) New total \n (2) Up to");
        int result = Integer.parseInt(sc.nextLine());
        setResult(result);
    }

    // input display method
    public void displayMethod(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Choose your display method \n (1) Tabular \n (2) Chart");
        int display = Integer.parseInt(sc.nextLine());
        setDisplay(display);
    }
}
