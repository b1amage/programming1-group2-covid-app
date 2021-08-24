import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class UserInterface {
    private static Scanner sc = new Scanner(System.in);
    private Data data;
    private int dividingNumber;
    private String groupingMethod;
    private String metric;
    private String result;
    private String display;
    private String location;
    private int timeRangeChoice;
    private TimeRange timeRange;
    private int dayOrWeekChoice;

    //Constructor
    public UserInterface(){}

    //Getters and Setters
    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getGroupingMethod() {
        return groupingMethod;
    }

    public void setGroupingMethod(String groupingMethod) {
        this.groupingMethod = groupingMethod;
    }

    public int getDividingNumber() {
        return dividingNumber;
    }

    public void setDividingNumber(int dividingNumber) {
        this.dividingNumber = dividingNumber;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getTimeRangeChoice() {
        return timeRangeChoice;
    }

    public void setTimeRangeChoice(int timeRangeChoice) {
        this.timeRangeChoice = timeRangeChoice;
    }

    public TimeRange getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(TimeRange timeRange) {
        this.timeRange = timeRange;
    }

    public int getDayOrWeekChoice() {
        return dayOrWeekChoice;
    }

    public void setDayOrWeekChoice(int dayOrWeekChoice) {
        this.dayOrWeekChoice = dayOrWeekChoice;
    }


    //Display UI
    public void displayUI() throws IOException {
        boolean isRunning = true;
        while (isRunning){
            boolean checkInputDataRange = false;
            boolean checkInputGroupingMethod = false;
            boolean checkDisplayMethod = false;
            // Ask for data range and check if user input right
            while(!checkInputDataRange) {
                inputDataRange();
                Data data = Data.createData(getLocation(), getTimeRange());
                data.createRowData();
                setData(data);
                if (getData().getRowsFromStartDate() == null){
                    checkInputDataRange = false;
                } else {
                    checkInputDataRange = true;
                }
            }
            // Ask for summary method
            while(!checkInputGroupingMethod) {
                inputGroupingMethod();
                Summary summary = Summary.createSummary(getData(), getGroupingMethod(), getMetric(), getResult(), getDividingNumber());
                summary.processData();
                if (summary.getGroupings() == null){
                    checkInputGroupingMethod = false;
                } else {
                    checkInputGroupingMethod = true;
                }
            }
            // Ask for display method
            displayMethod();

            // Ask the user if they want to continue
            System.out.println("===========================");
            System.out.println("Do you wish to continue \n (1) Yes \n (2) No");
            int cont = Integer.parseInt(sc.nextLine());
//            setData(data.getRowsFromStartDate());

            // If user choose 1 then continue, if 2 then break and stop the cycle
            isRunning = (1 == cont);
        }
    }

    // input grouping method
    public void inputGroupingMethod(){
        System.out.println("Choose your grouping method \n (1) No grouping \n (2) Number of groups \n (3) Number of days");
<<<<<<< HEAD
        char groupingChar = sc.nextLine().trim().charAt(0);
        if (Character.isDigit(groupingChar)) {
            int grouping = Integer.parseInt(String.valueOf(groupingChar));
            setGroupingMethod(grouping);
=======
        String groupingChar = sc.nextLine().trim();
        while (!groupingChar.equals("1") && !groupingChar.equals("2") && !groupingChar.equals("3")){
            System.out.println("Wrong option, please insert again: ");
            groupingChar = sc.nextLine().trim();
>>>>>>> 83adf203f899cb2cb493af53cd6fb68c848fead9
        }

        if (groupingChar.equals("1")) {
            setGroupingMethod("no grouping");
        }

        if (groupingChar.equals("2")){
            setGroupingMethod("number of groups");

            System.out.println("How many groups do you want?");
            char dividingChar = sc.nextLine().charAt(0);
            if (Character.isDigit(dividingChar)) {
                int dividing = Integer.parseInt(String.valueOf(dividingChar));
                setDividingNumber(dividing);
            }
        }

        if (groupingChar.equals("3")){
            setGroupingMethod("number of days");

            System.out.println("How many days in a group do you want?");
            char dividingChar = sc.nextLine().charAt(0);
            if (Character.isDigit(dividingChar)) {
                int dividing = Integer.parseInt(String.valueOf(dividingChar));
                setDividingNumber(dividing);
            }
        }

        System.out.println("Choose your metric \n (1) Positive cases \n (2) New deaths \n (3) People vaccinated");
        String metricChar = sc.nextLine().trim();
        while (!metricChar.equals("1") && !metricChar.equals("2") && !metricChar.equals("3")){
            System.out.println("Wrong option, please insert again: ");
            metricChar = sc.nextLine().trim();
        }

        if (metricChar.equals("1")) {
            setMetric("positive cases");
        }

        if (metricChar.equals("2")) {
            setMetric("new deaths");
        }

        if (metricChar.equals("3")) {
            setMetric("people vaccinated");
        }

        System.out.println("Choose your result type \n (1) New total \n (2) Up to");
        String resultChar = sc.nextLine().trim();

        if (resultChar.equals("1")) {
            setResult("new total");
        }

        if (resultChar.equals("2")) {
            setResult("up to");
        }
    }

    // Input display method
    public void displayMethod(){
        System.out.println("Choose your display method \n (1) Tabular \n (2) Chart");
        String displayChar = sc.nextLine().trim();
        while (!displayChar.equals("1") && !displayChar.equals("2")){
            System.out.println("Wrong option, please insert again: ");
            displayChar = sc.nextLine().trim();
        }

        if (displayChar.equals("1")) {
            setDisplay("tabular");
        }

        if (displayChar.equals("2")) {
            setDisplay("chart");
        }
    }

    // Input data method
    public void inputDataRange(){
        System.out.println("Choose your location:");
        String location = sc.nextLine().trim();
        setLocation(location);

        // ask user to choose
        showDateChoiceMenu();
        String timeRangeChar = sc.nextLine().trim();
        while (!timeRangeChar.equals("1") && !timeRangeChar.equals("2") && !timeRangeChar.equals("3")){
            System.out.println("Wrong option, please insert again: ");
            timeRangeChar = sc.nextLine().trim();
        }

        int timeRangeChoice = Integer.parseInt(timeRangeChar);
        setTimeRangeChoice(timeRangeChoice);
        // Check user input for time range
        setTimeRangeFromChoice();

    }

    // Input time range
    public void setTimeRangeFromChoice(){
        int nextDayCount = 0;
        switch(timeRangeChoice){
            case 1:
                System.out.println("Enter start date");
                String startDate = sc.nextLine();

                System.out.println("Enter end date");
                String endDate = sc.nextLine();
                timeRange = new TimeRange(startDate, endDate, 0);
                break;

            case 2:
            case 3: //User enter the date days and choose how many days or week from the start
                System.out.println("Enter your start date");
                startDate = sc.nextLine();

                System.out.println("Use (1) weeks or (2) days");
                char daysOrWeeksChar = sc.nextLine().charAt(0);
                if (Character.isDigit(daysOrWeeksChar)){
                    int daysOrWeeks = Integer.parseInt(String.valueOf(daysOrWeeksChar));
                    setDayOrWeekChoice(daysOrWeeks);
                }
                if (dayOrWeekChoice == 1){
                    System.out.println("Enter your weeks");
                    nextDayCount = Integer.parseInt(sc.nextLine()) * 7;
                }
                else {
                    System.out.println("Enter your days");
                    nextDayCount = Integer.parseInt(sc.nextLine());
                }
                timeRange = new TimeRange(startDate, null, nextDayCount);
                timeRange = timeRangeChoice == 2 ? new TimeRange(startDate,null, nextDayCount) : new TimeRange(startDate,null, -nextDayCount);
                break;

        }

    }

    // Show data menu
    public void showDateChoiceMenu() {
        System.out.println("Enter your date choice: ");
        System.out.println("(1) A pair of start date and end date (inclusive) (e.g., 1/1/2021 and 8/1/2021)");
        System.out.println("(2) A number of days or weeks from a particular date (e.g., 2 days from 1/20/2021 " +
                "means there are 3 days 1/20/2021, 1/21/2021, and 1/22/2021)");
        System.out.println("(3) A number of days or weeks to a particular date " +
                "(e.g., 1 week to 1/8/2021 means there are 8 days from 1/1/2021 to 1/8/2021)");
    }

    // Main
    public static void main(String[] args) throws IOException {
        UserInterface userInterface = new UserInterface();
        userInterface.displayUI();
    }
}
