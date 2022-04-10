package proj.one;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileManipulation implements DataManipulation {
    private int index = 50000;
    private final int estimatedIdx = 1;
    private final int lodgementIdx = 2;
    private final int quantityIdx = 3;
    private final int salesmanIdx = 4;
    private final int modelIdx = 5;
    private final int contractIdx = 6;

    private Map<String, String[]> getCenterMap() {
        String item;
        String[] splitCenter;
        Map<String, String[]> rst = new HashMap<>();
        try (BufferedReader bufferedReader = new BufferedReader(
            new FileReader("proj1_public_client_enterprises.csv"))) {
            while ((item = bufferedReader.readLine()) != null) {
                splitCenter = item.split(",");
                String[] parts = new String[5];
                parts[0] = splitCenter[1];
                parts[1] = splitCenter[2];
                parts[2] = splitCenter[3];
                parts[3] = splitCenter[4];
                parts[4] = splitCenter[5];
                rst.put(splitCenter[0], parts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rst;
    }

    private Map<String, String[]> getOrderMap() {
        String item;
        String[] splitOrders;
        Map<String, String[]> rst = new HashMap<>();
        try (BufferedReader bufferedReader = new BufferedReader(
            new FileReader("proj1_public_orders.csv"))) {
            while ((item = bufferedReader.readLine()) != null) {
                splitOrders = item.split(",");
                String[] parts = new String[6];
                parts[0] = splitOrders[estimatedIdx];
                parts[1] = splitOrders[lodgementIdx];
                parts[2] = splitOrders[quantityIdx];
                parts[3] = splitOrders[salesmanIdx];
                parts[4] = splitOrders[modelIdx];
                parts[5] = splitOrders[contractIdx];
                rst.put(splitOrders[0], parts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rst;
    }

    private Map<String, String[]> getContractMap() {
        String item;
        String[] splitContract;
        Map<String, String[]> rst = new HashMap<>();
        try (BufferedReader bufferedReader = new BufferedReader(
            new FileReader("proj1_public_contracts.csv"))) {
            while ((item = bufferedReader.readLine()) != null) {
                splitContract = item.split(",");
                String[] parts = new String[2];
                parts[0] = splitContract[1];
                parts[1] = splitContract[2];
                rst.put(splitContract[0], parts);
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
            writer.write(index + "," + str + "\n");
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    @Override
    public String selectOrderById(int id) {
        Map<String, String[]> maps = getOrderMap();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("order_id: ").append(id).append("\n");
        String[] infos = maps.get(String.valueOf(id));
        stringBuilder.append("estimated_delivery_date: ").append(infos[0]).append("\n");
        stringBuilder.append("lodgement_date: ").append(infos[1]).append("\n");
        stringBuilder.append("quantity: ").append(infos[2]).append("\n");
        stringBuilder.append("salesman_id: ").append(infos[3]).append("\n");
        stringBuilder.append("model_id: ").append(infos[4]).append("\n");
        stringBuilder.append("contract_number: ").append(infos[5]).append("\n");
        return stringBuilder.toString();
    }

    @Override
    public String selectOrderByTimeInterval(String est, String log) {
        String item;
        StringBuilder stringBuilder = new StringBuilder();
        String[] splitOrders;
        try (BufferedReader bufferedReader = new BufferedReader(
            new FileReader("proj1_public_orders.csv"))) {
            while ((item = bufferedReader.readLine()) != null) {
                splitOrders = item.split(",");
                if (splitOrders[estimatedIdx].equals(est)) {
                    if (splitOrders[lodgementIdx] == null && log == null) {
                        stringBuilder.append(splitOrders[0]).append(";");
                    } else if (splitOrders[lodgementIdx] != null &&
                        splitOrders[lodgementIdx].equals(log)) {
                        stringBuilder.append(splitOrders[0]).append(";");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    @Override
    public String selectContractOfEnterById(int id) {
        try {
            Map<String, String[]> orders = getOrderMap();
            Map<String, String[]> cons = getContractMap();
            Map<String, String[]> enters = getCenterMap();
            String contract = orders.get(String.valueOf(id))[contractIdx - 1];
            String enter = cons.get(contract)[1];
            return enters.get(enter)[0];
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String selectIdsBySameSalesman(int id) {
        String item;
        String[] splitOrders;
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(
            new FileReader("proj1_public_orders.csv"))) {
            while ((item = bufferedReader.readLine()) != null) {
                splitOrders = item.split(",");
                if (id == Integer.parseInt(splitOrders[salesmanIdx])) {
                    stringBuilder.append(splitOrders[0]).append(";");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    @Override
    public int deleteById(int id) {
        Map<String, String[]> map = getOrderMap();
        File file = new File("proj1_public_orders.csv");
        map.remove(String.valueOf(id));
        String item;
        String[] arr;
        try (BufferedReader bufferedReader = new BufferedReader(
            new FileReader("proj1_public_orders.csv"))) {
            bufferedReader.readLine();
            while ((item = bufferedReader.readLine()) != null) {
                arr = item.split(",");
                if (arr[0].equals(String.valueOf(id))) {
                    FileWriter fileWriter = new FileWriter(file, false);
                    fileWriter.write("");
                    fileWriter.close();
                    break;
                }
            }
            for (int i = 1; i <= map.size(); i++) {
                String[] temp = map.get(String.valueOf(i));
                FileWriter fw = new FileWriter(file, true);
                fw.write(String.valueOf(i) + "," + temp[0] +
                    "," + temp[1] + "," + temp[2] +
                    "," + temp[3] + "," + temp[4] +
                    "," + temp[5]);
                fw.write(System.getProperty("line.separator"));
                fw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int updateLogById(int id, String date) {
        Map<String, String[]> map = getOrderMap();
        File file = new File("proj1_public_orders.csv");
        String[] temp = map.get(String.valueOf(id));
        temp[1] = date;
        map.put(String.valueOf(id), temp);
        try {
            FileWriter fileWriter = new FileWriter(file, false);
            fileWriter.write("");
            fileWriter.close();
            for (int i = 1; i <= map.size(); i++) {
                String[] t = map.get(String.valueOf(i));
                FileWriter fw = new FileWriter(file, true);
                fw.write(String.valueOf(i) + "," + t[0] +
                    "," + t[1] + "," + t[2] +
                    "," + t[3] + "," + t[4] +
                    "," + t[5]);
                fw.write(System.getProperty("line.separator"));
                fw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
