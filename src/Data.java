import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Data {
    private ArrayList<Row> rows = DataProcess.createRowList();
    private ArrayList<Row> rowsFromStartDate = new ArrayList<Row>();
    private String continent;
    private String country;
    private String startDate;
    private String endDate;
    private int nextDayCount;

    public Data() throws FileNotFoundException {}

    public void test() {
        System.out.println(endDate == null);
    }

    public void createRowData() {
        if (endDate != null) {
            int startIndex = -1;
            int endIndex = -1;
            for (Row row : rows) {
                if (row.getDate().equals(startDate) && (row.getContinent().equals(continent) || row.getLocation().equals(country))) {
                    startIndex = rows.indexOf(row);
                }
                if (row.getDate().equals(endDate) && (row.getContinent().equals(continent) || row.getLocation().equals(country))) {
                    endIndex = rows.indexOf(row);
                }
            }

            if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
                for (int i = startIndex; i <= endIndex; i++) {
                    rowsFromStartDate.add(rows.get(i));
                }
            } else {
                System.out.println("Error in date, country, or continent");
            }
        } else {
            for (Row row : rows) {
                if (row.getDate().equals(startDate) && (row.getContinent().equals(continent) || row.getLocation().equals(country))) {
                    for (int i = Math.min(rows.indexOf(row),rows.indexOf(row) + nextDayCount); i <= Math.max(rows.indexOf(row),rows.indexOf(row) + nextDayCount) && i < rows.size() && i > -1; i++) {
                        if (rows.get(i) != null) {
                            rowsFromStartDate.add(rows.get(i));
                        }
                    }
                }
            }
        }
    }

    public Data(String continent, String country, String startDate, String endDate) throws FileNotFoundException {
        this.continent = continent;
        this.country = country;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Data(String continent, String country, String startDate, int nextDayCount) throws FileNotFoundException {
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

    public static Data createData() throws FileNotFoundException {
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
//                String[] splitStartDate = startDate.split("/");
//                String[] splitEndDate = endDate.split("/");
//                int dayCount = (Integer.parseInt(splitEndDate[2]) - Integer.parseInt(splitStartDate[2]))*365 + (Integer.parseInt(splitEndDate[0]) - Integer.parseInt(splitStartDate[0])) * 30 +  (Integer.parseInt(splitEndDate[1]) - Integer.parseInt(splitStartDate[1]));
//                data.setNextDayCount(dayCount);
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

    public ArrayList<Row> getRows() {
        return rows;
    }

    public ArrayList<Row> getRowsFromStartDate() {
        return rowsFromStartDate;
    }
}
