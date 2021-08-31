/*
  Class: UserInterface
  Purpose: Get input from the user and display result to the console
  Contributors: Minh Long, Quoc Bao, Kha Tuan, Anh Duy
  Created date: 14/8/2021
  Last modified: 26/8/2021
  Version 1.0
 */

import java.util.Scanner;

public class UserInterface {
    // Fields
    private static final Scanner sc = new Scanner(System.in); // For avoid duplicate code of init scanner

    // Data fields
    private Data data;
    private int timeRangeChoice;
    private TimeRange timeRange;
    private int dayOrWeekChoice;
    private String location;

    // Summary fields
    private Summary summary;
    private int dividingNumber;
    private String groupingMethod;
    private String metric;
    private String result;

    // UserInterface field
    private String display;

    // Constructor
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

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    // Logic handle methods

    /**
     * This function ask user to input their option and display the resut
     */
    public void displayUI() {
        boolean isRunning = true;
        while (isRunning){

            boolean checkInputTimeRange = false;
            boolean checkInputGroupingMethod = false;
            boolean checkDisplayMethod = false;
            boolean checkInputLocation = false;

            // Ask to enter location
            while (!checkInputLocation) {
                inputLocation();
                Data tempData = new Data();
                checkInputLocation = !(tempData.isLocationNotExist(location));

                if (!checkInputLocation) {
                    System.out.println("Location not found, please try again");
                }
            }

            // Ask for data range and check if user input right
            while(!checkInputTimeRange) {
                inputTimeRange();
                Data data = Data.createData(getLocation(), getTimeRange());
                data.createRowData();
                setData(data);

                // If the time range has error, ask user to input again
                while (getData().isHasTimeRangeError()) {
                    timeRange = TimeRange.setTimeRangeFromChoice(timeRangeChoice);
                    data = Data.createData(getLocation(), getTimeRange());
                    data.createRowData();
                    setData(data);
                }
                checkInputTimeRange = getData().getRowsFromStartDate() != null;
            }

            // Ask for summary method
            while(!checkInputGroupingMethod) {
                inputGroupingMethod();
                Summary tempSummary = Summary.createTempSummary(getData(), getGroupingMethod(), getDividingNumber());
                setSummary(tempSummary);
                checkInputGroupingMethod = summary.isValidGroupingMethod();
            }

            inputMetricAndResultType();
            Summary summary = Summary.createSummary(getData(), getGroupingMethod(), getMetric(), getResult(), getDividingNumber());
            summary.processData();
            setSummary(summary);

            // Ask for display method
            while (!checkDisplayMethod) {
                inputDisplayMethod();
                Display display = Display.createDisplay(getSummary(), getDisplay());
                display.displayData();
                checkDisplayMethod = display.getDisplayMethod() != null;
            }

            // Ask the user if they want to continue
            displayContinueOrNot();

            String cont = sc.nextLine().trim();

            // If user choose 1 then continue, if 2 then break and stop the cycle
            isRunning = (cont.equals("1"));
        }
    }


    /**
     * This function ask user to input their desire grouping method
     */
    public void inputGroupingMethod(){
        System.out.println("Choose your grouping method \n (1) No grouping \n (2) Number of groups \n (3) Number of days");
        String groupingChar = sc.nextLine().trim();
        while (!groupingChar.equals("1") && !groupingChar.equals("2") && !groupingChar.equals("3")){
            System.out.println("Wrong option, please insert again: ");
            groupingChar = sc.nextLine().trim();

        }
        checkAndSetGroupingMethod(groupingChar);
    }
    /**
     * This function ask user to input their desire metric and result types
     */
    public void inputMetricAndResultType() {
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
        while (!resultChar.equals("1") && !resultChar.equals("2")){
            System.out.println("Wrong option, please insert again: ");
            resultChar = sc.nextLine().trim();
        }

        if (resultChar.equals("1")) {
            setResult("new total");
        }

        if (resultChar.equals("2")) {
            setResult("up to");
        }
    }

    /**
     *Base on the grouping method option, this function check which option the user choose
     * @param grouping option the user choose
     */
    public void checkAndSetGroupingMethod(String grouping) {
        if (grouping.equals("1")) {
            setGroupingMethod("no grouping");
        }

        if (grouping.equals("2")){
            setGroupingMethod("number of groups");

            System.out.println("How many groups do you want?");
            String dividingChar = sc.nextLine().trim();
            while (!dividingChar.matches("[0-9]+")) {
                System.out.println("This is not a valid number, please insert again: ");
                dividingChar = sc.nextLine().trim();
            }
            int dividing = Integer.parseInt(dividingChar);
            setDividingNumber(dividing);
        }

        if (grouping.equals("3")){
            setGroupingMethod("number of days");

            System.out.println("How many days in a group do you want?");
            String dividingChar = sc.nextLine().trim();
            while (!dividingChar.matches("[0-9]+")) {
                System.out.println("This is not a valid number, please insert again: ");
                dividingChar = sc.nextLine().trim();
            }
            int dividing = Integer.parseInt(dividingChar);
            setDividingNumber(dividing);
        }
    }

    /**
     * This function help user input the data range
     */
    public void inputTimeRange(){
        // Ask user to choose
        showDateChoiceMenu();
        String timeRangeChar = sc.nextLine().trim();

        while (!timeRangeChar.equals("1") && !timeRangeChar.equals("2") && !timeRangeChar.equals("3")){
            System.out.println("Wrong option, please insert again: ");
            timeRangeChar = sc.nextLine().trim();
        }

        int timeRangeChoice = Integer.parseInt(timeRangeChar);
        setTimeRangeChoice(timeRangeChoice);

        // Check user input for time range
        timeRange = TimeRange.setTimeRangeFromChoice(timeRangeChoice);

    }

    /**
     * This function get input of location from user
     */
    public void inputLocation() {
        System.out.println("Choose your location (you can choose either country or continent)");
        String location = sc.nextLine().trim();

        setLocation(location);
    }


    /**
     * This function help user choose display option
     */
    public void inputDisplayMethod(){
        System.out.println("Choose your display method \n (1) Tabular \n (2) Chart");
        String displayChar = sc.nextLine().trim();
        while (!displayChar.equals("1") && !displayChar.equals("2")){
            System.out.println("Wrong option, please insert again ");
            displayChar = sc.nextLine().trim();
        }

        if (displayChar.equals("1")) {
            setDisplay("tabular");
        }

        if (displayChar.equals("2")) {
            setDisplay("chart");
        }
    }

    // Display methods

    /**
     * This function display date choice
     */
    public void showDateChoiceMenu() {
        System.out.println("Enter your date choice: ");
        System.out.println("(1) A pair of start date and end date (inclusive) (e.g., 1/1/2021 and 1/8/2021)");
        System.out.println("(2) A number of days or weeks from a particular date (e.g., 2 days from 1/20/2021 " +
                "means there are 3 days 1/20/2021, 1/21/2021, and 1/22/2021)");
        System.out.println("(3) A number of days or weeks to a particular date " +
                "(e.g., 1 week to 1/8/2021 means there are 8 days from 1/1/2021 to 1/8/2021)");
    }

    /**
     * This function ask user to continue or exit the program
     */
    public void displayContinueOrNot() {
        System.out.println("===========================");
        System.out.println("Do you wish to continue? \n (1) Yes \n (*) No");
    }

}
