package proj.one;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class FileManipulation implements DataManipulation {
    private int index = 50000;

    private Map<String, String> getCountryMap() {
        String item;
        String[] splitOrders;
        Map<String, String> rst = new HashMap<>();

        try (BufferedReader bufferedReader = new BufferedReader(
            new FileReader("proj1_public_orders.csv"))) {
            bufferedReader.readLine();
            while ((item = bufferedReader.readLine()) != null) {
                splitOrders = item.split(",");
                rst.put(splitOrders[0], item.substring(splitOrders[0].length() + 1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rst;
    }

    @Override
    public void openDatasource() {

    }

    @Override
    public void closeDatasource() {

    }

    @Override
    public int addOrder(String str) {
        try (FileWriter writer = new FileWriter("proj1_public_orders.csv", true)) {
            index++;
            writer.write(index + "," + str);
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    @Override
    public String selectOrderById(int id) {

        return null;
    }

    @Override
    public String selectOrderByTimeInterval(String est, String log) {
        return null;
    }

    @Override
    public String selectContractOfEnterById(int id) {
        return null;
    }

    @Override
    public String selectIdsBySameSalesman(int id) {
        return null;
    }

    @Override
    public int deleteById(int id) {
        return 0;
    }

    @Override
    public int updateLogById(int id, Date date) {
        return 0;
    }
}
