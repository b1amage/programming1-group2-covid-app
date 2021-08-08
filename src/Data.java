import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Data {
    protected static ArrayList<Row> rows;

    static {
        try {
            rows = DataProcess.createRowList();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String continent;
    private String country;
    private String startDate;
    private String endDate;
    private int nextDayCount;

    public Data() {}

    public Data(String continent, String country, String startDate, String endDate) {
        this.continent = continent;
        this.country = country;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Data(String continent, String country, String startDate, int nextDayCount) {
        this.continent = continent;
        this.country = country;
        this.startDate = startDate;
        this.nextDayCount = nextDayCount;
    }


    public static void showDateChoiceMenu() {
        System.out.println("Enter your date choice: ");
        System.out.println("(1) A pair of start date and end date (inclusive) (e.g., 1/1/2021 and 8/1/2021)");
        System.out.println("(2) A number of days or weeks from a particular date (e.g., 2 days from 1/20/2021 " +
                "means there are 3 days 1/20/2021, 1/21/2021, and 1/22/2021)");
        System.out.println("(3) A number of days or weeks to a particular date " +
                "(e.g., 1 week to 1/8/2021 means there are 8 days from 1/1/2021 to 1/8/2021)");
    }

    public static Data createData() {
        Data data = new Data();
        Scanner sc = new Scanner(System.in);
        System.out.println("Use country (1) or continent (2)?");
        int areaChoice = Integer.parseInt(sc.nextLine());
        String country;
        String continent;
        if (areaChoice == 1) {
            System.out.println("Enter country: ");
            country = sc.nextLine();
            continent = "";
        } else {
            System.out.println("Enter continent: ");
            continent = sc.nextLine();
            country = "";
        }

        data.setCountry(country);
        data.setContinent(continent);

        showDateChoiceMenu();
        int dateChoice = Integer.parseInt(sc.nextLine());
        String startDate;
        String endDate;
        int day;
        int dayOrWeekChoice;


        switch (dateChoice) {
            case 1:
                System.out.println("Enter start date: ");
                startDate = sc.nextLine();
                System.out.println("Enter end date: ");
                endDate = sc.nextLine();
                data.setStartDate(startDate);
                data.setEndDate(endDate);
                break;
            case 2:
                System.out.println("Enter date: ");
                startDate = sc.nextLine();
                System.out.println("Use week (1) or day (2)?");
                dayOrWeekChoice = Integer.parseInt(sc.nextLine());

                if (dayOrWeekChoice == 1) {
                    System.out.println("Enter weeks: ");
                    day = Integer.parseInt(sc.nextLine()) * 7;
                } else {
                    System.out.println("Enter days: ");
                    day = Integer.parseInt(sc.nextLine());
                }

                data.setStartDate(startDate);
                data.setNextDayCount(day);
                break;

            case 3:
                System.out.println("Enter date: ");
                startDate = sc.nextLine();
                System.out.println("Use week (1) or day (2)?");
                dayOrWeekChoice = Integer.parseInt(sc.nextLine());

                if (dayOrWeekChoice == 1) {
                    System.out.println("Enter weeks: ");
                    day = -Integer.parseInt(sc.nextLine()) * 7;
                } else {
                    System.out.println("Enter days: ");
                    day = -Integer.parseInt(sc.nextLine());
                }

                data.setStartDate(startDate);
                data.setNextDayCount(day);
                break;
        }
        return data;
    }

    public void display() {
        System.out.println("Continent: " + continent);
        System.out.println("Country: " + country);
        System.out.println("Start date: " + startDate);
        System.out.println("End date: " + endDate);
        System.out.println("Next day count: " + nextDayCount);
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getNextDayCount() {
        return nextDayCount;
    }

    public void setNextDayCount(int nextDayCount) {
        this.nextDayCount = nextDayCount;
    }

}
