package proj.one;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;

public class FileManipulation implements DataManipulation {
    private static int index;
    private static final StampedLock sl = new StampedLock();
    private static Map<String, String[]> orders = new HashMap<>();
    private static final Map<String, String[]> enters = new HashMap<>();
    private static final Map<String, String[]> models = new HashMap<>();
    private static final Map<String, String[]> sales = new HashMap<>();
    private static final Map<String, String[]> contracts = new HashMap<>();

    static {
        getHashmapResult("proj1_public_orders.csv", 6);
        getHashmapResult("proj1_public_salesmen.csv", 6);
        getHashmapResult("proj1_public_models.csv", 3);
        getHashmapResult("proj1_public_contracts.csv", 2);
        getHashmapResult("proj1_public_client_enterprises.csv", 5);
        int max = 0;
        for (String key : orders.keySet()) {
            if (Integer.parseInt(key) > max) {
                max = Integer.parseInt(key);
            }
        }
        index = max;
    }

    public FileManipulation() throws IOException {
    }

    static void getHashmapResult(String filename, int len) {
        String item;
        String[] fragment;
        try (BufferedReader bufferedReader = new BufferedReader(
            new FileReader(filename))) {
            while ((item = bufferedReader.readLine()) != null) {
                fragment = item.split(",");
                String[] parts = new String[len];
                System.arraycopy(fragment, 1, parts, 0, len);
                if (filename.contains("orders")) {
                    orders.put(fragment[0], parts);
                } else if (filename.contains("models")) {
                    models.put(fragment[0], parts);
                } else if (filename.contains("client")) {
                    enters.put(fragment[0], parts);
                } else if (filename.contains("contracts")) {
                    contracts.put(fragment[0], parts);
                } else {
                    sales.put(fragment[0], parts);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void openDatasource() {

    }

    @Override
    public void closeDatasource() {
    }


    private String searchForWrite(String str, String[] arr) {
        String[] temp = str.split(";");
        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(index).append(",");
        arr[0] = temp[0];
        arr[1] = temp[1];
        arr[2] = temp[2];
        arr[5] = temp[5];
        stringBuilder.append(temp[0]).append(",");
        stringBuilder.append(temp[1]).append(",");
        stringBuilder.append(temp[2]).append(",");
        String[] infos;
        int size_salesman, size_model;
        size_salesman = sales.size();
        size_model = models.size();
        long stamp = sl.tryOptimisticRead();
        for (int i = 1; i <= size_salesman; i++) {
            infos = sales.get(String.valueOf(i));
            if (infos[2].equals(temp[3])) {
                stringBuilder.append(i).append(",");
                arr[3] = String.valueOf(i);
                break;
            }
        }
        for (int i = 1; i <= size_model; i++) {
            infos = models.get(String.valueOf(i));
            if (infos[0].equals(temp[4])) {
                stringBuilder.append(i).append(",");
                arr[4] = String.valueOf(i);
                break;
            }
        }
        if (!sl.validate(stamp)) {
            stamp = sl.readLock();
            stringBuilder = new StringBuilder();
            try {
                for (int i = 1; i <= size_salesman; i++) {
                    infos = sales.get(String.valueOf(i));
                    if (infos[2].equals(temp[3])) {
                        stringBuilder.append(infos[0]).append(",");
                        arr[3] = String.valueOf(i);
                        break;
                    }
                }
                for (int i = 1; i <= size_model; i++) {
                    infos = models.get(String.valueOf(i));
                    if (infos[1].equals(temp[4])) {
                        stringBuilder.append(infos[0]).append(",");
                        arr[4] = String.valueOf(i);
                        break;
                    }
                }
            } finally {
                sl.unlockRead(stamp);
            }
        }
        stringBuilder.append(temp[5]);
        return stringBuilder + "\n";
    }

    @Override
    public int addOrder(String str) {
        String[] infos = new String[6];
        String content = searchForWrite(str, infos);
        int res = 1;
        long stamp = sl.writeLock();
        try {
            index++;
            FileWriter fw = new FileWriter("proj1_public_orders.csv", true);
            fw.write(index + "," + content);
            fw.close();
            orders.put(String.valueOf(index), infos);
        } catch (IOException e) {
            e.printStackTrace();
            res = 0;
        } finally {
            sl.unlockWrite(stamp);
        }
        return res;
    }

    //将order转成输出string;
    static String orderToString(int id, String[] infos) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("order_id: ").append(id).append("\n");
        stringBuilder.append("estimated_delivery_date: ").append(infos[0]).append("\n");
        stringBuilder.append("lodgement_date: ").append(infos[1]).append("\n");
        stringBuilder.append("quantity: ").append(infos[2]).append("\n");
        stringBuilder.append("salesman_id: ").append(infos[3]).append("\n");
        stringBuilder.append("model_id: ").append(infos[4]).append("\n");
        stringBuilder.append("contract_number: ").append(infos[5]).append("\n");
        return stringBuilder.toString();
    }

    @Override
    public String selectOrderById(int id) {
        String[] infos;
        long stamp = sl.tryOptimisticRead();
        infos = orders.get(String.valueOf(id));
        if (!sl.validate(stamp)) {
            stamp = sl.readLock();
            try {
                infos = orders.get(String.valueOf(id));
            } finally {
                sl.unlockRead(stamp);
            }
        }
        return orderToString(id, infos);
    }

    @Override
    public String selectOrderByTimeInterval(String est, String log) {
        StringBuilder stringBuilder;
        String[] infos;
        int size;
        long stamp = sl.tryOptimisticRead();
        size = orders.size();
        stringBuilder = new StringBuilder();
        for (int i = 1; i <= size; i++) {
            infos = orders.get(String.valueOf(i));
            if (infos != null) {
                if (infos[0].equals(est)) {
                    if ((log == null && infos[1] == null) || (infos[1] != null &&
                        infos[1].equals(log))) {
                        stringBuilder.append(i).append("\n");
                    }
                }
            }
        }
        if (!sl.validate(stamp)) {
            stamp = sl.readLock();
            stringBuilder = new StringBuilder();
            try {
                size = orders.size();
                for (int i = 1; i <= size; i++) {
                    infos = orders.get(String.valueOf(i));
                    if (infos != null) {
                        if (infos[0].equals(est)) {
                            if ((log == null && infos[1] == null) || (infos[1] != null &&
                                infos[1].equals(log))) {
                                stringBuilder.append(i).append("\n");
                            }
                        }
                    }
                }
            } finally {
                sl.unlockRead(stamp);
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public String selectContractOfEnterById(int id) {
        String contract, enter, res;
        long stamp = sl.tryOptimisticRead();
        contract = orders.get(String.valueOf(id))[5];
        enter = contracts.get(contract)[1];
        res = enters.get(enter)[0];
        if (!sl.validate(stamp)) {
            stamp = sl.readLock();
            try {
                contract = orders.get(String.valueOf(id))[5];
                enter = contracts.get(contract)[1];
                res = enters.get(enter)[0];
            } finally {
                sl.unlockRead(stamp);
            }
        }
        return res;
    }

    @Override
    public String selectIdsBySameSalesman(int id) {
        StringBuilder stringBuilder;
        int size;
        String[] infos;
        long stamp = sl.tryOptimisticRead();
        size = orders.size();
        stringBuilder = new StringBuilder();
        for (int i = 1; i <= size; i++) {
            infos = orders.get(String.valueOf(i));
            if (infos != null) {
                if (id == Integer.parseInt(infos[3])) {
                    stringBuilder.append(i).append(";");
                }
            }
        }
        if (!sl.validate(stamp)) {
            stamp = sl.readLock();
            stringBuilder = new StringBuilder();
            try {
                size = orders.size();
                stringBuilder = new StringBuilder();
                for (int i = 1; i <= size; i++) {
                    infos = orders.get(String.valueOf(i));
                    if (infos != null) {
                        if (id == Integer.parseInt(infos[3])) {
                            stringBuilder.append(i).append(";");
                        }
                    }
                }
            } finally {
                sl.unlockRead(stamp);
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public int deleteById(int id) {
        String filename = "proj1_public_orders.csv";
        String[] infos;
        int size;
        long stamp = sl.writeLock();
        try {
            orders.remove(String.valueOf(id));
            FileWriter fw_clear = new FileWriter(filename, false);
            fw_clear.write("");
            fw_clear.close();
            size = orders.size();
            FileWriter fw_text = new FileWriter(filename, true);
            for (int i = 1; i <= size; i++) {
                infos = orders.get(String.valueOf(i));
                if (infos != null) {
                    fw_text.write(i + "," + infos[0] +
                        "," + infos[1] + "," + infos[2] +
                        "," + infos[3] + "," + infos[4] +
                        "," + infos[5]);
                    fw_text.write(System.getProperty("line.separator"));
                }
            }
            fw_text.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sl.unlockWrite(stamp);
        }
        return 0;
    }

    @Override
    public int updateLogById(int id, String date) {
        String[] infos;
        int size;
        String filename = "proj1_public_orders.csv";
        long stamp = sl.tryOptimisticRead();
        infos = orders.get(String.valueOf(id));
        if (!sl.validate(stamp)) {
            stamp = sl.readLock();
            try {
                infos = orders.get(String.valueOf(id));
            } finally {
                sl.unlockRead(stamp);
            }
        }
        infos[1] = date;
        stamp = sl.writeLock();
        try {
            orders.put(String.valueOf(id), infos);
            FileWriter fw_clear = new FileWriter(filename, false);
            fw_clear.write("");
            fw_clear.close();
            size = orders.size();
            FileWriter fw_text = new FileWriter(filename, true);
            for (int i = 1; i <= size; i++) {
                infos = orders.get(String.valueOf(i));
                if (infos != null) {
                    fw_text.write(i + "," + infos[0] +
                        "," + infos[1] + "," + infos[2] +
                        "," + infos[3] + "," + infos[4] +
                        "," + infos[5]);
                    fw_text.write(System.getProperty("line.separator"));
                }
            }
            fw_text.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sl.unlockWrite(stamp);
        }
        return 0;
    }
}
