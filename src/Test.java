public class Test {
    public static void main(String[] args) {
        Data data = Data.createData();
        data.display();
        DataProcess.displayRows(Data.rows);
    }
}
