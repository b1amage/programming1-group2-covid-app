public class Data {
    private String area;
    private String startDate;
    private String endDate;

    public Data(String area, String startDate, String endDate) {
        this.area = area;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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
}
