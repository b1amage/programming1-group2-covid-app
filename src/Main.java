import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here
        Scanner sc = new Scanner(new File("src/data.csv"));
        ArrayList<String> dataByRow = new ArrayList<String>();
        ArrayList<HashMap<String, String>> dataSplit = new ArrayList<HashMap<String, String>>();
//        Iterator iterator = dataByRow.iterator();


        while (sc.hasNextLine()) {
            dataByRow.add(sc.nextLine());
        }

        String[] title = dataByRow.get(0).split(",");

        for (int i = 1; i < dataByRow.size(); i++) {
            HashMap<String, String> tempMap = new HashMap<String, String>();
            String[] tempData = dataByRow.get(i).split(",");

            if (tempData.length == title.length) {
                for (int j = 0; j < title.length; j++) {
                    tempMap.put(title[j], tempData[j]);
                }

                dataSplit.add(tempMap);
            }
        }

        for (HashMap<String, String> hashMap : dataSplit) {
            printHashMap(hashMap);
        }

//        printArrayList(dataByRow);
//        System.out.println(dataByRow.get(0));
    }

    public static void printArrayList(ArrayList<String> arrayList) {
        for (String row : arrayList) {
            System.out.println(row);
        }
    }

    public static void printHashMap(HashMap<String, String> map) {
        for (String name: map.keySet()) {
            String key = name.toString();
            String value = map.get(name).toString();
            System.out.println(key + " " + value);
        }
        System.out.println("=========================");
    }


}
