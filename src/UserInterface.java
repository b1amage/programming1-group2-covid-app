import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class UserInterface {
    private ArrayList<Row> data;
    private int groupingMethod;
    private int metric;
    private int result;
    private int display;


    //constructor
    public UserInterface(){}


    //getter setter
    public ArrayList<Row> getData() {
        return data;
    }

    public void setData(ArrayList<Row> data) {
        this.data = data;
    }

    public int getGroupingMethod() {
        return groupingMethod;
    }

    public void setGroupingMethod(int groupingMethod) {
        this.groupingMethod = groupingMethod;
    }

    public int getMetric() {
        return metric;
    }

    public void setMetric(int metric) {
        this.metric = metric;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }

    private static Scanner sc = new Scanner(System.in);

    //display UI
    public void displayUI() throws IOException {
        boolean isRunning = true;
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

            // If user choose 1 then continue, if 2 then break and stop the cycle
            isRunning = (1 == cont);
        }
    }

    // input grouping method
    public void inputGroupingMethod(){
        System.out.println("Choose your grouping method \n (1) No grouping \n (2) Number of groups \n (3) Number of days");
        char groupingChar = sc.nextLine().charAt(0);
        if (Character.isDigit(groupingChar)) {
            int grouping = Integer.parseInt(String.valueOf(groupingChar));
            setGroupingMethod(grouping);
        }

        System.out.println("Choose your metric \n (1) Positive cases \n (2) New deaths \n (3) People vaccinated");
        char metricChar = sc.nextLine().charAt(0);
        if (Character.isDigit(metricChar)) {
            int metric = Integer.parseInt(String.valueOf(metricChar));
            setMetric(metric);
        }

        System.out.println("Choose your result type \n (1) New total \n (2) Up to");
        char resultChar = sc.nextLine().charAt(0);
        if (Character.isDigit(resultChar)) {
            int result = Integer.parseInt(String.valueOf(resultChar));
            setResult(result);
        }
    }

    // input display method
    public void displayMethod(){
        System.out.println("Choose your display method \n (1) Tabular \n (2) Chart");
        char displayChar = sc.nextLine().charAt(0);
        if (Character.isDigit(displayChar)) {
            int display = Integer.parseInt(String.valueOf(displayChar));
            setDisplay(display);
        }
    }


    // main
    public static void main(String[] args) throws IOException {
        UserInterface ui = new UserInterface();
        ui.displayUI();
    }
}
